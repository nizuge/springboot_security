package cn.anytec.security;

import cn.anytec.mongo.MongoDB;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

@Component
public class IdentityDetailService implements UserDetailsService{

    private static final Logger logger = LoggerFactory.getLogger(IdentityDetailService.class);

    @Autowired
    MongoDB mongoDB;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Document result = mongoDB.getUser_collection().find(eq("name",username)).first();
        if(result==null){
            throw new UsernameNotFoundException(username+" not exist");
        }
        logger.info("用户登录："+username);
        Integer access = null;
        try {
            access = result.getInteger("access");
        }catch (Exception e){
            logger.error("用户:"+username+"权限等级获取失败");
            logger.error(e.getMessage());
            access = 1;
        }
        //装配到UserDetails，相当于生成了一个<user>标签
        UserDetails userDetails = new User(result.getString("name"),result.getString("password"), true, true, true, true,getAuthorities(access) );

        return userDetails;
    }

    public Collection<GrantedAuthority> getAuthorities(int access){
        List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>(3);
        authList.add(new SimpleGrantedAuthority("ROLE_USER"));
        if(access==4){
            authList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }else if(access == 7){
            authList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            authList.add(new SimpleGrantedAuthority("ROLE_SYS"));
        }
        return authList;
    }

}
