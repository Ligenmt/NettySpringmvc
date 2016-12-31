package com.ligen.dao;


import java.net.UnknownHostException;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientOptions.Builder;
import com.mongodb.MongoClientURI;
import com.mongodb.WriteConcern;

/**
 * @author liude
 *
 */
public abstract class AbstractMongoDao
{
  
  protected final String aliasName ;
  
  protected MongoTemplate mongoTemplate ;
  
  public AbstractMongoDao(String aliasName)
  {
    this.aliasName = aliasName;
  }

  /**
   * mongoTemplate的方法抛出的是RuntimeException, 不会被显式的被捕捉, 所以强制抛出Exception类型, 以避免无法发现错误
   */
  //实现类中应返回具体的MongoTemplate
  public MongoTemplate getMongoTemplate() throws Exception 
  {
    if (this.mongoTemplate == null)
    {
      mongoTemplate = buildMongoTemplate(aliasName) ;
    }
    return this.mongoTemplate ;
  }

  
  protected MongoTemplate buildMongoTemplate(String aliasName) throws UnknownHostException
  {
    
    String configName = String.format("mongo-%s", aliasName) ;
    
    ResourceBundle bundle = ResourceBundle.getBundle(configName) ;
    
    String replicaSet = bundle.getString(String.format("mongo.%s.replica-set", aliasName)) ;
    String database = bundle.getString(String.format("mongo.%s.database", aliasName)) ;
    String username = bundle.getString(String.format("mongo.%s.username", aliasName)) ;
    String password = bundle.getString(String.format("mongo.%s.password", aliasName)) ;
    

    String uri = String.format("mongodb://%s:%s@%s/%s", username, password, replicaSet, database) ;
    
    String connectionsPerHost = bundle.getString(String.format("mongo.%s.connections-per-host", aliasName)) ;
    String connectTimeout = bundle.getString(String.format("mongo.%s.connect-timeout", aliasName)) ;
    String maxWaitTime = bundle.getString(String.format("mongo.%s.max-wait-time", aliasName)) ;
    String socketKeepAlive = bundle.getString(String.format("mongo.%s.socket-keep-alive", aliasName)) ;
    String socketTimeout = bundle.getString(String.format("mongo.%s.socket-timeout", aliasName)) ;
    String threadsAllowedToBlockForConnectionMultiplier = bundle.getString(String.format("mongo.%s.threads-allowed-to-block-for-connection-multiplier", aliasName)) ;
    String writeConcern = bundle.getString(String.format("mongo.%s.write-concern", aliasName)) ;
    
    Builder builder = MongoClientOptions.builder()
        .connectionsPerHost(Integer.parseInt(connectionsPerHost))
        .connectTimeout(Integer.parseInt(connectTimeout))
        .maxWaitTime(Integer.parseInt(maxWaitTime))
        .socketKeepAlive(Boolean.parseBoolean(socketKeepAlive))
        .socketTimeout(Integer.parseInt(socketTimeout))
        .threadsAllowedToBlockForConnectionMultiplier(Integer.parseInt(threadsAllowedToBlockForConnectionMultiplier))
        .writeConcern(WriteConcern.valueOf(writeConcern))
        ;
    
    return new MongoTemplate(new MongoClient(new MongoClientURI(uri,builder)), database);
  }
  
  
  /**
   * 保存到mongodb 
   * obj中如果没有"_id"或者"id"属性, 则在保存的时候mongodb会自动生成一个"_id"属性
   * @param collectionName
   * @param obj
   */
  public void save(String collectionName, Object obj)
  {
    long start = System.currentTimeMillis() ;
    try
    {
      getMongoTemplate().save(obj, collectionName) ;
    } catch (Exception e)
    {
      e.printStackTrace(); 
    }
  }
  
  /**
   * 不指定类型的时候， 按照指定的id, 将Object保存到mongodb
   * Object会先转换为com.alibaba.fastjson.JSONObject, 附加上"_id"值, 再进行保存
   * @param collectionName
   * @param id
   * @param obj
   */
  public void save(String collectionName, Object id, Object obj)
  {
    long start = System.currentTimeMillis() ;
    JSONObject jsonObj = convert2JSONObject(id, obj) ;
    
    try
    {
      getMongoTemplate().save(jsonObj, collectionName);
      
    } catch (Exception e)
    {
      e.printStackTrace(); 
    }
    
  }
  
  /**
   * 按照id值, 指定的类型, 查找Object
   * @param collectionName
   * @param id
   * @param clazz
   * @return
   */
  public <T> T findById(String collectionName, Object id, Class<T> clazz)
  {
    T t = null ;
    try
    {
      t = getMongoTemplate().findById(id, clazz, collectionName) ;
    } catch (Exception e)
    {
      e.printStackTrace(); 
    }
    return t ; 
  }
  
  
  /**
   * 按照id值, 指定的类型, 查找Object
   * @param collectionName
   * @param id
   * @param clazz
   * @return
   */
  public <T> T findOne(String collectionName, Object id, Class<T> clazz)
  {
    long start = System.currentTimeMillis() ;
    Query query = new Query() ;
    query.addCriteria(new Criteria("_id").is(id)) ;
    T t = null ;
    try {
      t = getMongoTemplate().findOne(query, clazz, collectionName) ;
    } catch (Exception e)
    {
      e.printStackTrace(); 
    }
    
    return t ; 
  }
 
  //指定_id的情况下, 将object转换为JSONObject
  static private JSONObject convert2JSONObject(Object id, Object obj)
  {
    if (obj == null) return null ;
    
    JSONObject json = (JSONObject)JSONObject.toJSON(obj) ;
    
    json.put("_id", id) ;
    
    return json ;
    
  }
}
