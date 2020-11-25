# Embedded Qpid 

At this age and time we shouldn't have code which is not tested and when it comes to testing AMQP properly, 
you just have to find a way to spin up an AMQP broker/server to run your tests. This project attempts to
resolve this issue by using QPID as an AMQP 0.9.1 broker; typically when you are using RabbitMQ.

This project is specifically for Spring Boot and it for testing purposes only. Having said that feel free to modify
and/or help me improve it for anyone who needs it.

## Getting Started

Simply add the dependency to your Spring Boot project and it will automatically start up when you run the 
Spring Boot context

```xml
<dependency>
  <groupId>net.clintonmagro.embedded.qpid</groupId>
  <artifactId>embedded-qpid-spring-boot-starter</artifactId>
  <version>1.1.0</version>
  <scope>test</scope>
</dependency>
```

## Running the tests

Once the dependency is in your project, when Spring Boot loads the context, QPID will automatically run on 
the default AMQP port 5672. If you would like to change this, it can be done using any of the default
Spring Boot's property injectors using property *embedded.qpid.port*

### vanilla example:
```java
@SpringBootTest
@RunWith(SpringRunner.class)
public class SomeIntegrationTest {
  
  @Test
  public void someTest() {
    //....amqp specific code
    //....at this point QPID is up and AMQP is available on the port 5672 or depending on the port specified
  }
}
```

### random port example:
if you specify port 0, QPID will launch on a random port instead of the default 5672. If this is your preference,
you may want to Autowire *net.clintonmagro.embedded.qpid.broker.EmbeddedBroker* and get the port using *getPort()*

## Versioning

We use [JGitFlow Plugin](https://bitbucket.org/atlassian/jgit-flow/wiki/Home/) for versioning. For the versions available, see the [tags on this repository](https://github.com/clintonmagro/embedded-qpid-spring-boot-starter/tags). 

## Authors

* **Clinton Magro** - clintonmagro@gmail.com

## License

This project is licensed under the Apache License - see the [LICENSE](LICENSE) file for details

## Acknowledgments

* Necessity is the mother of all inventions and this is what this project is about. There are other projects out there that have tried to tackle this problem 
and in this project I tried to do the same however make it the Spring Boot way; with a Starter.