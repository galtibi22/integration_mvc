package smartspace.data.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import smartspace.AppProperties;
import smartspace.data.ElementEntity;
import smartspace.data.Location;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.layout.UserBoundary;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class EntityGenerator {
    private Random random=new Random();
    private List<User> randomUsers=new ArrayList<User>();
    private Logger logger= Logger.getLogger(Thread.currentThread().getClass().getName());
    private AppProperties properties;
    private EntityFactory entityFactory=new EntityFactoryImpl();

    @Autowired
    public void setAppProperties(AppProperties prop){
        properties=prop;
    }

    @Autowired
    public void initRandomData(@Value("${generator.usersjson}") String userJson) throws IOException {
        URL url=Thread.currentThread().getContextClassLoader().getResource(userJson);
        ObjectMapper mapper = new ObjectMapper();
        User[]users=new User[0];
        randomUsers=Arrays.asList((User[])mapper.readValue(url,users.getClass()));
    }
    public UserEntity userEntity(UserRole role,String smartspace){
        User user= randomUser();
        return entityFactory.createNewUser(user.getEmail(),smartspace,user.getFirstName()+user.getLastName(),properties.AVATAR,role, new Random().nextInt(100));
    }
    public UserBoundary userBoundary(UserRole role){
        return new UserBoundary(userEntity(role,properties.getName()));
    }

    public ArrayList<UserEntity>  userEntities(int num,UserRole role){
        ArrayList<UserEntity> userEntities=new ArrayList<>();
        for (int i=0;i<num;i++){
            UserEntity userEntity=userEntity(role,properties.getName());
            logger.info("UserEntities was generated" + userEntities.toString());
            userEntities.add(userEntity);
        }
        return userEntities;
    }

    public ArrayList<UserBoundary> userBoundaries(int num,UserRole role,String smartspace){
        ArrayList<UserBoundary> users=new ArrayList<>();
        for (int i=0;i<num;i++){
            UserBoundary userBoundary=new UserBoundary(userEntity(role,smartspace));
            logger.info("UserBoundary was generated" + userBoundary.toString());
            users.add(userBoundary);
        }
        return users;
    }


    public ElementEntity elementEntity(){
        return elementEntity(properties.getName());
    }

    public ElementEntity elementEntity(String smartspace){
        User user=randomUser();
        return entityFactory.createNewElement(user.getFullName(), properties.TYPE, new Location(random.nextInt(),random.nextInt()),new Date(),
                user.getEmail(), smartspace, false, new HashMap<>());
    }

        public List<ElementEntity> elementList(int size) {
            return elementList(size,properties.getName());
        }

    public List<ElementEntity> elementList(int size,String smartspace) {

        return IntStream.range(0, size).mapToObj(i->elementEntity(smartspace)).collect(Collectors.toList());

    }

        private User randomUser(){
        return randomUsers.get( new Random().nextInt(randomUsers.size()));
    }
}
