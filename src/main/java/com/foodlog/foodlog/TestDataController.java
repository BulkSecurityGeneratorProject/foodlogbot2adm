package com.foodlog.foodlog;

import com.foodlog.domain.*;
import com.foodlog.foodlog.gateway.telegram.ApiUrlBuilder;
import com.foodlog.foodlog.gateway.telegram.model.GetFile;
import com.foodlog.foodlog.gateway.telegram.model.Update;
import com.foodlog.foodlog.report.bodylog.BodyLogImage;
import com.foodlog.repository.*;
import com.foodlog.security.AuthoritiesConstants;
import com.foodlog.service.util.RandomUtil;
import com.google.common.io.ByteStreams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by rafael on 18/01/18.
 */
@RestController
@RequestMapping("/test")
public class TestDataController {

    @Autowired
    private BodyLogRepository bodyLogRepository;

    @Autowired
    private WeightRepository weightRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MealLogRepository mealLogRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @CrossOrigin(origins = "*")
    @RequestMapping("/user")
    public User createUser(@RequestParam(value="telegram") Long telegram, @RequestParam(value="name") String name) {
        User user = new User();
        user.setLogin(name);
        user.setFirstName(name);
        user.setLastName(name);
        //user.setEmail("teste@teste.com");
        user.setLangKey("en"); // default language

        Authority authority = new Authority();
        authority.setName(AuthoritiesConstants.USER);
        Set<Authority> authorities = new HashSet<>();
        authorities.add(authorityRepository.findOne(AuthoritiesConstants.USER));
        user.setAuthorities(authorities);

        String encryptedPassword = passwordEncoder.encode("teste");
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);
        userRepository.save(user);
        return user;

    }



        @CrossOrigin(origins = "*")
    @RequestMapping("/init")
    public Boolean init(@RequestParam(value="userid") Long userid,
                                     @RequestParam(value="base-date") Instant initDate) {


        User user = userRepository.findOne(userid);

        bodyLogRepository.deleteAll();
        createBodyLog(initDate.minus(7, ChronoUnit.DAYS), "/images/body1.png", user);
        createBodyLog(initDate.minus(5, ChronoUnit.DAYS), "/images/body2.png", user);
        createBodyLog(initDate.minus(3, ChronoUnit.DAYS), "/images/body3.png", user);
        createBodyLog(initDate.minus(1, ChronoUnit.DAYS), "/images/body4.png", user);


        weightRepository.deleteAll();
        createWeight(initDate.minus(1, ChronoUnit.DAYS), user);
        createWeight(initDate.minus(2, ChronoUnit.DAYS), user);
        createWeight(initDate.minus(3, ChronoUnit.DAYS), user);
        createWeight(initDate.minus(4, ChronoUnit.DAYS), user);
        createWeight(initDate.minus(5, ChronoUnit.DAYS), user);
        createWeight(initDate.minus(6, ChronoUnit.DAYS), user);
        createWeight(initDate.minus(7, ChronoUnit.DAYS), user);

        mealLogRepository.deleteAll();
        for (int i=1; i<=7; i++){
            for (int j=1; j<=5; j++) {
                createMealog(initDate.minus(i, ChronoUnit.DAYS), user);
            }
        }
        return true;
    }

    private void createMealog(@RequestParam(value = "base-date") Instant initDate, User user) {
        MealLog mealLog = new MealLog();
        mealLog.setPhoto(getPicture("/images/meal.jpg"));
        mealLog.setRating(((new Random().nextInt() & Integer.MAX_VALUE) % 5) + 1);
        mealLog.setUpdateId(new Random().nextLong());
        mealLog.setUser(user);
        mealLog.setPhotoContentType("image/jpg");
        mealLog.setMealDateTime(initDate);

        mealLogRepository.save(mealLog);
    }

    private void createWeight(@RequestParam(value = "base-date") Instant initDate, User user) {
        Weight weight = new Weight();
        weight.setUpdateId(new Random().nextLong());
        weight.setWeightDateTime(initDate);
        Float w = 90F + ((new Random().nextInt() & Integer.MAX_VALUE) % 10);
        weight.setValue(w);
        weight.setUser(user);
        weightRepository.save(weight);
    }

    private void createBodyLog(@RequestParam(value = "base-date") Instant initDate, String file_path, User user) {
        BodyLog bodyLog = new BodyLog();
        bodyLog.setBodyLogDatetime(initDate);
        bodyLog.setUser(user);
        bodyLog.setPhotoContentType("image/jpg");

        bodyLog.setPhoto(getPicture(file_path));
        bodyLog.setUpdateId(new Random().nextLong());

        bodyLogRepository.save(bodyLog);
    }

    public byte[] getPicture(String file_path) {
       /* URI getBytesurl = ApiUrlBuilder.getBytesUrl(file_path);
        return new RestTemplate().getForObject(getBytesurl, byte[].class);*/
        InputStream imagem = this.getClass().getResourceAsStream(file_path);
        try {
            return ByteStreams.toByteArray(imagem);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

}
