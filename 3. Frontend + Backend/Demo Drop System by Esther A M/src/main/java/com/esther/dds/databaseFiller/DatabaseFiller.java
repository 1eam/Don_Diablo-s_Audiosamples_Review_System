package com.esther.dds.databaseFiller;

/**
 * This class serves to fill initial data in the database
 * Data such as fictional users and their roles (e.g. Role of reviewer or admin)
 * This class will always execute first at startup
 */

import com.esther.dds.domain.*;
import com.esther.dds.repositories.*;
import com.esther.dds.service.AdminService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
public class DatabaseFiller implements CommandLineRunner {
    private UserRepository userRepository;
    private BoUserRepository boUserRepository;
    private AdminService adminService;
    private RoleRepository roleRepository;
    private BoRoleRepository boRoleRepository;
    private AdminRoleRepository adminRoleRepository;

    public DatabaseFiller(UserRepository userRepository, BoUserRepository boUserRepository, AdminService adminService, RoleRepository roleRepository, BoRoleRepository boRoleRepository, AdminRoleRepository adminRoleRepository) {
        this.userRepository = userRepository;
        this.boUserRepository = boUserRepository;
        this.adminService = adminService;
        this.roleRepository = roleRepository;
        this.boRoleRepository = boRoleRepository;
        this.adminRoleRepository = adminRoleRepository;
    }

    //Initialization of hashmaps that will contain all entries for the database
    //Admin-hashmap not present cause the system currently supports one Admin.
    Map<String, Demo> demos = new HashMap<String, Demo>();
    Map<String, State> states = new HashMap<String, State>();
    Map<String, User> users = new HashMap<String, User>();
    Map<String, BoUser> boUsers = new HashMap<String, BoUser>();

    //This is the password that all generated users get: 0000
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    String secret = "{bcrypt}" + encoder.encode("0000");

    //Interface implementation of Commandline runner, responsible for execution at startup
    //Fills database with manually generated users and registers the admins account
    @Override
    public void run(String... args) throws Exception {
        registerAdminAccount();
        fillDBWithGeneratedUsers();
        fillDBWithGeneratedBOUsers();
    }

    private void registerAdminAccount() {
        //Save rolename to DB
        AdminRole adminRole = new AdminRole("ROLE_ADMIN");
        adminRoleRepository.save(adminRole);

        //Registers the admin user: sets account enabled to false, generates the random password, and sends authentication-email with pw + activationcode. This occurs at every startup.
        Admin admin = new Admin("info@admin.com");
        adminService.register(admin, adminRole);
    }

    private void fillDBWithGeneratedUsers() {
        Role userRole = new Role("ROLE_USER");
        roleRepository.save(userRole);

        users.put("user1", new User("info@garrix.com", secret, "Martijn", "Garritssen", "Martin Garrix", "I thought, You know what? You might need another Talent to recruit" , "/serverside_profileimages/martijn.jpg",true));
        users.put("user2", new User("martine.dijkraam@yahoo.com", secret, "Martine", "Dijkraam", "DJ Martine", "Love makin music, Love gettin inspired by nature, Mexican food are the best" , "/serverside_profileimages/martine.jpg",true));
        users.put("user3", new User("c.deberg@@yahoo.com", secret, "Christopher", "de Berg", "DJ Christopher", "Im Christopher, Im a dutch-based musicproducer with love for house music I make deep-house, future-house, tech-house, and bass house. I study computer science at the University of Amsterd" , "/serverside_profileimages/christopher.jpg",true));
        users.put("user4", new User("HB@gmail.com", secret, "Johanna", "Aaltonen", "HB", "HB perustettiin vuonna 2002 Forssassa. Yhtye teki seitsemän albumia. HB-kirjainten sanotaan valittu sattumanvaraisesti eivätkä oikeasti tarkoita mitään, mutta niille on annettu Pyhän Raamatun merkitys." , "/serverside_profileimages/hb.jpg",true));
        users.put("user5", new User("jawqt59114@tempmail.com", secret, "Theodore", "Bagwell", "Scylla 5", "Im in the music bussiness for over 11 years now. Been on stage whith Martin Garrix, Steve Ayoki and Kshmr several times. In fact reqruited them at Spinning records. And now its my time to shine. Listen to my music and see that I'm worthy" , "/serverside_profileimages/theodore.jpg",true));
        users.put("user6", new User("info@kurtkennedey.com", secret, "Kurt", "Kennedey", "Katarpillar", "A accountant with a business and passion for mixing and producing" , "/serverside_profileimages/kurt.jpg",true));
        users.put("user7", new User("berder3@myspace.com", secret, "Brandon ", "Guillintinni", "Sweet Icing", "roses are red, violets are blue, i love edm, you should too" , "/serverside_profileimages/brandon.jpg",true));

        users.forEach((k,v) -> {
            v.addRole(userRole);
            v.setConfirmPassword(secret);
            userRepository.save(v);
        });
    }

    private void fillDBWithGeneratedBOUsers() {
        BoRole boUserRole = new BoRole("ROLE_BO-USER");
        boRoleRepository.save(boUserRole);

        boUsers.put("deletedbouser", new BoUser("deletedbouser.com", secret, "Deleted user", "Deleted user",true));
        boUsers.put("bo1", new BoUser("info@bo.com", secret, "Floris", "Roddelaar",true));
        boUsers.put("bo2", new BoUser("info@bo2.com", secret, "Kasper", "D. HoogZoon",true));
        boUsers.put("bo3", new BoUser("info@bo3.com", secret, "Joris", "Dobbelaar",true));
        boUsers.put("bo4", new BoUser("info@bo4.com", secret, "Daphne", "van Veldt",true));

        boUsers.forEach((k,v) -> {
            v.addBoRole(boUserRole);
            v.setConfirmPassword(secret);
            boUserRepository.save(v);
        });
    }

    //Predefined Tracks, These tracks are stored in the resources/static/serverside_audiofiles folder
    @Bean
    CommandLineRunner runner(DemoRepository demoRepository, StateNameRepository stateNameRepository) {
        return args -> {
            //set review-states
            states.put("state1", new State("Pending", "The Admin should set a 'In-review message'"));
            states.put("state2", new State("Rejected", "The Admin should enter a 'Rejection message'"));
            states.put("state3", new State("Sent", "The Admin should enter a 'Sent message'"));

            //Store review states
            states.forEach((k,v) -> {
                stateNameRepository.save(v);
            });

            String prefixPath = "/serverside_audiofiles/";

            //Individual Demo entries
            demos.put("demo1", new Demo("Deep House 4", "Emme halunneet antaa kuvausta. anteeksi", prefixPath + "1. sana.mp3"));
            demos.put("demo2", new Demo("Adventure", "I used 13 different vst's, to much to name in this description. I also mastered the track with some help of a friend, who also found me a vocalist. Great Right!?", prefixPath + "2. adventure.mp3"));
            demos.put("demo3", new Demo("Virus", "You Gotta love it, it was a hit song", prefixPath + "3. virus.mp3"));
            demos.put("demo4", new Demo("Love is Forgiveness", "This is the result of an experimental project that turned into a full blown deep house track produced in october 2017, Prager University. Vocals are done by Marcian Patzrelek.", prefixPath + "4. love is forgiveness.mp3"));
            demos.put("demo5", new Demo("Trans", " ", prefixPath + "5. trans.mp3"));
            demos.put("demo6", new Demo("$Wande - Bands$", "$tay ble$$ed", prefixPath + "6. bands.mp3"));
            demos.put("demo7", new Demo("Lily-Jo – SWAY feat. Dawn Elektra", "I did the production for this song which was first an acoustic. The singers wanted an 'upbeat' deep house version and I was asked to make the production for this one. Please focus on the background music ", prefixPath + "7. sway.mp3"));
            demos.put("demo8", new Demo("I got you", "Please listen to this sample I have here. Its short, I hope you like it", "/serverside_audiofiles/8. i got you.mp3"));
            demos.put("demo9", new Demo("NoBody", "I used 13 different vst's, to much to name in this description. I also mastered the track with some help of a friend, who also found me a vocalist. Great Right!?", prefixPath + "9. nobody.mp3"));
            demos.put("demo10", new Demo("Triangles", "Good vibes!", prefixPath + "10. triangles.mp3"));
            demos.put("demo11", new Demo("Adventure Mashup", "Remaked the original production by DJ Sweet Icing, and added some great vibes", prefixPath + "11. adventure mashup.mp3"));
            demos.put("demo12", new Demo("Mazza", "I was in my yard when i heard two shots go of... them tings flew right past. watch my videoclip: https://www.youtube.com/watch?v=tbrz5Xlyy9Q", prefixPath + "12. mazza.mp3"));
            demos.put("demo13", new Demo("Hanki Elämä III", "Sata vastustaa, sata puolustaa, sata huutaa muuten vaan Sukupolvia samat ongelmat taas vaivaa Kuka kasteen saa kuka kastetaan Kuka leivän viinin jakaa Tuskin selvyyttä tähän koskaan saadaan Etkö antaisi olla", prefixPath + "13. hanki elama 3.mp3"));
            demos.put("demo14", new Demo("It Is Time.mp3", "Oletko pelastettu oikeasti? Nyt elämäsi on ihanteellinen? Oletko tehnyt kotiisi vankilan? Entä komissio? Jumala on antanut sinulle niin paljon Saatatko tavoittaa kadonneet? Käveletkö ohitse ajattelematta Niistä, joilla ei ole toivoa? Isä taivaassa, auta meitä antautumaan Jos et vastaa...", prefixPath + "14. it is time.mp3"));
            demos.put("demo15", new Demo("Kremlin", "Wanna play good music at an edm party? listen to this", prefixPath + "15. kremlin.mp3"));
            demos.put("demo16", new Demo("Brazilian bass", "I really hope you liked this one. Its not finished yet, but i was very excited to release this early for youto hear. Also its not officially released yet. make sure to check out my youtube: https://youtube.com/youtube", prefixPath + "16. brazilian bass.mp3"));
            demos.put("demo17", new Demo("All day", "watch the videoclip at: https://www.youtube.com/watch?v=PHWvKQBleMY", prefixPath + "17. all day.mp3"));
            demos.put("demo18", new Demo("Special kind of love", " This time I added some Jazz logic in my production. Hope youll love it", prefixPath + "18. special kind of love.mp3"));
            demos.put("demo19", new Demo("Keys", " ... ", prefixPath + "19. keys.mp3"));
            demos.put("demo20", new Demo("Skynight", " Worked together with a singer on this one. Did the production with a little help, but mastered the song all by myself ", prefixPath + "20.skynight.mp3"));
            demos.put("demo21", new Demo("Gos Edm", " Gospel EDM", prefixPath + "21. gospel-edm.mp3"));
            demos.put("demo22", new Demo("Wizard", " my followup hitsong after Animals ", prefixPath + "22. wizard.mp3"));
            demos.put("demo23", new Demo("Proxy", " Your difinantly love it... ", prefixPath + "23. proxy.mp3"));
            demos.put("demo24", new Demo("She got Aesthetic", " ..Wande.. ", prefixPath + "24. aesthetic.mp3"));

            //Assign demos to a review state: 'pending review', 'Rejected', 'Sent' (= forwarded to Don Diablo)
            demos.forEach((k,v) -> {
                if (k== "demo16"||k== "demo5"||k== "demo23"){
                    v.setState(states.get("state3")); //Sent state
                } else if (k== "demo8"||k== "demo9"||k== "demo19"||k== "demo20"){
                    v.setState(states.get("state2")); //Rejected state
                } else {
                    v.setState(states.get("state1")); //the rest is Pending state
                }
            });

            //Assign demos to user (uploader)
            demos.forEach((k,v) -> {
                if (k== "demo3"||k== "demo22"||k== "demo23"){
                    v.setUser(users.get("user1"));
                } else if (k== "demo4"||k== "demo5"||k== "demo8"){
                    v.setUser(users.get("user2"));
                } else if (k== "demo9"||k== "demo15"||k== "demo20"||k== "demo16"){
                    v.setUser(users.get("user3"));
                } else if (k== "demo1"||k== "demo13"||k== "demo14"){
                    v.setUser(users.get("user4"));
                } else if (k== "demo6"||k== "demo12"||k== "demo17"||k== "demo24"){
                    v.setUser(users.get("user5"));
                } else if (k== "demo7"||k== "demo11"||k== "demo18"||k== "demo21"){
                    v.setUser(users.get("user6"));
                } else if (k== "demo2"||k== "demo10"||k== "demo19"){
                    v.setUser(users.get("user7"));
                }
            });

            //Assign back-office reviewer to a demo:
            demos.get("demo8").setReviewedBy(boUsers.get("bo2"));
            demos.get("demo9").setReviewedBy(boUsers.get("bo3"));
            demos.get("demo19").setReviewedBy(boUsers.get("bo2"));
            demos.get("demo20").setReviewedBy(boUsers.get("bo4"));
            demos.get("demo5").setReviewedBy(boUsers.get("bo2"));
            demos.get("demo16").setReviewedBy(boUsers.get("bo3"));
            demos.get("demo23").setReviewedBy(boUsers.get("bo4"));

            //save all demos.
            demos.forEach((k,v) -> {
                demoRepository.save(v);
            });
        };
    }
}


