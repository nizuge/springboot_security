package cn.anytec.mongo;

import cn.anytec.config.GeneralConfig;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

@Component
public class MongoDB implements MongoDBService {
    @Autowired
    GeneralConfig config;

    private static final Logger logger = LoggerFactory.getLogger(MongoDB.class);
    private final MongoClient mongoClient = new MongoClient("127.0.0.1");
    private final MongoDatabase database = mongoClient.getDatabase("security");
    private final MongoCollection<Document> user_collection = database.getCollection("user");

    public MongoDB(){
        logger.info("======= 初始化MongoDB =======");
    }

    public static void main(String[] args) {
        MongoDB mongoDB = new MongoDB();
        MongoCollection<Document> role = mongoDB.getUser_collection();
        Document document = new Document();
        document.append("name","admin").append("password","admin").append("access",4);
        role.insertOne(document);
    }

    public MongoCollection<Document> getUser_collection() {
        return user_collection;
    }
}
