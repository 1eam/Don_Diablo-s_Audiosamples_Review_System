package com.diabolo.api;

import com.diabolo.api.annotation.PathMapper;
import com.diabolo.api.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PathHandler {
    private static final String[] EMPTY_STRING_ARRAY = {};
    private static final String DEFAULT_PATH_SEPARATOR = "/";
    private static final Class<AjaxResponseController> CLAZZ = AjaxResponseController.class;
    private static HttpServletResponse response;
    private static HttpServletRequest request;

    public static void handleRequest(String path, HttpServletResponse httpServletResponse,
                                     HttpServletRequest httpServletRequest) throws Exception {
        response = httpServletResponse;
        request = httpServletRequest;
        String[] pathArray = tokenizeToStringArray(path, DEFAULT_PATH_SEPARATOR);

        outerloop:
        for (Method method : CLAZZ.getDeclaredMethods()) {
            if (!method.isAnnotationPresent(PathMapper.class)) {
                continue;
            }
            PathMapper pathMapper = method.getAnnotation(PathMapper.class);
            if (pathMapper.value().equals("")) {
                continue;
            }
            String[] patternArray = tokenizeToStringArray(pathMapper.value(), DEFAULT_PATH_SEPARATOR);

            int patternLength = patternArray.length;
            int pathLength = pathArray.length;
            if (patternLength != pathLength) {
                continue;
            }

            method.setAccessible(true);
            if (isArgumentPresent(patternArray)) {
                for (int i = 0; i < findFirstArgument(patternArray); i++) {
                    if (!patternArray[i].equals(pathArray[i])) {
                        continue outerloop;
                    }
                }
            } else {
                for (int i = 0; i < patternLength; i++) {
                    if (!patternArray[i].equals(pathArray[i])) {
                        continue outerloop;
                    }
                }
                method.invoke(CLAZZ.getDeclaredConstructor(getArgumentsClass())
                        .newInstance(getArgumentsObject()));
            }
            mapParametersToPath(method, patternArray, pathArray);
            /*Object[] objects = new Object[3];
            objects[0] = "test";
            objects[1] = 121212;
            objects[2] = "een string";
            mapParametersToPath(method, patternArray, pathArray, objects);*/
            return;
        }
    }

    private static Object[] getArgumentsObject() {
        return new Object[] { response, request };
    }

    private static Class[] getArgumentsClass() {
        return new Class[] { HttpServletResponse.class, HttpServletRequest.class };
    }

    private static boolean stringHasArgument(String string) {
        Pattern pattern = Pattern.compile("\\{(.*?)\\}");
        Matcher matcher = pattern.matcher(string);
        return matcher.find();
    }

    private static boolean isArgumentPresent(String[] patternArray) {
        for (int i = 0; i < patternArray.length; i++) {
            if (stringHasArgument(patternArray[i])) {
                return true;
            }
        }
        return false;
    }

    private static int findFirstArgument(String[] patternArray) {
        for (int i = 0; i < patternArray.length; i++) {
            if (stringHasArgument(patternArray[i])) {
                return i;
            }
        }
        return 0;
    }

    private static int countParamAnnotations(Annotation[][] annotationArray) {
        int count = 0;
        for (Annotation[] annotationRow : annotationArray) {
            if (annotationRow.length > 0) {
                count++;
            }
        }
        return count;
    }

    private static void mapParametersToPath(Method method, String[] patternArray, String[] pathArray) throws Exception {
        mapParametersToPath(method, patternArray, pathArray, new Object[]{});
    }

    private static void mapParametersToPath(Method method, String[] patternArray,
                                            String[] pathArray, Object[] params) throws Exception {
        Parameter[] parameters = method.getParameters();
        int countParam = parameters.length;
        Object[] objects = new Object[countParam];
        boolean hasAnnotations = false;
        int countAnno = countParamAnnotations(method.getParameterAnnotations());
        int annoCounter = pathArray.length - countAnno;

        for (int i = 0; i < countParam; i++) {
            if (hasParameterAnnotation(parameters[i], PathVariable.class)) {
                String typeName = parameters[i].getType().getSimpleName();
                switch (typeName) {
                    case "String":
                        objects[i] = pathArray[annoCounter];
                        break;
                    case "int":
                        try {
                            objects[i] = Integer.valueOf(pathArray[annoCounter]);
                        } catch (NumberFormatException ex) {
                            objects[i] = 0;
                        }
                        break;
                    case "double":
                        try {
                            objects[i] = Double.valueOf(pathArray[annoCounter]);
                        } catch (NumberFormatException ex) {
                            objects[i] = 0;
                        }
                        break;
                    case "long":
                        try {
                            objects[i] = Long.valueOf(pathArray[annoCounter]);
                        } catch (NumberFormatException ex) {
                            objects[i] = 0;
                        }
                        break;
                }
                annoCounter++;
                hasAnnotations = true;
            }
        }
        if ((countParam != countAnno) && (params.length > 0)) {
            int counter = 0;
            for (int i = 0; i < objects.length; i++) {
                if (objects[i] == null) {
                    objects[i] = params[counter];
                    counter++;
                }
            }
        }

        if (hasAnnotations) {
            method.invoke(CLAZZ.getDeclaredConstructor(getArgumentsClass())
                    .newInstance(getArgumentsObject()), objects);
        } else {
            method.invoke(CLAZZ.getDeclaredConstructor(getArgumentsClass())
                            .newInstance(getArgumentsObject()), mapMaker(patternArray, pathArray));
        }
    }

    private static boolean hasParameterAnnotation(Parameter parameter, Class<? extends Annotation> annotationClass) {
        return parameter.isAnnotationPresent(annotationClass);
    }

    private static Map<String, String> mapMaker(String[] patternArray, String[] pathArray) {
        Map<String, String> output = new HashMap<>();
        for (int i = 1; i < patternArray.length; i++) {
            String key = params(patternArray[i]);
            String value = pathArray[i];
            output.put(key, value);
        }
        return output;
    }

    private static String params(String value) {
        Pattern pattern = Pattern.compile("\\{(.*?)\\}");
        Matcher matcher = pattern.matcher(value);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    private static String[] tokenizeToStringArray(String str, String delimeters) {
        if (str == null) {
            return EMPTY_STRING_ARRAY;
        }

        StringTokenizer st = new StringTokenizer(str, delimeters);
        List<String> tokens = new ArrayList<>();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (token.length() > 0) {
                tokens.add(token);
            }
        }
        return toStringArray(tokens);
    }

    private static String[] toStringArray(Collection<String> collection) {
        return collection.toArray(EMPTY_STRING_ARRAY);
    }
}
