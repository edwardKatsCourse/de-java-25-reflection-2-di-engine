package org.example.engine;

import lombok.SneakyThrows;
import org.example.engine.annotations.Autowired;
import org.example.engine.annotations.Service;
import org.example.engine.annotations.Value;
import org.example.project.Runner;
import org.reflections.Reflections;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ClassPathDIEngine {
    private final Map<Class<?>, Object> applicationContext = new HashMap<>();

    private final String scanPackage;

    public ClassPathDIEngine(String scanPackage) {
        this.scanPackage = scanPackage;
    }

    public void start() {
        // reflections.org
        // Phase 1 - find and create

        // 1. All classes inside org.example.project
        new Reflections("org.example.project")

        // 2. Give me all classes with annotation Service.class
                .getTypesAnnotatedWith(Service.class)

        // 3. new .. Service()
                .forEach(service -> {
                    try {
                        applicationContext.put(service, service.newInstance());
                    } catch (InstantiationException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });

        injectDependencies();
        injectValueAnnotation();
    }

    private void injectDependencies() {
        // Phase 2
        // Configure services

        // 1. All services in Application Context
        applicationContext
                .entrySet()
                .forEach(servicePair -> {
                    // UserService
                    //        @Autowired
                    //      - UserRepository userRepository
                    Class<?> serviceClass = servicePair.getKey();
                    Object instance = servicePair.getValue();

                    // 2. Find all fields to Inject
                    Arrays.stream(serviceClass.getDeclaredFields())

                            // 3. Find all fields with @Autowired annotation
                            .filter(field -> field.isAnnotationPresent(Autowired.class))
                            .forEach(field -> {
                                field.setAccessible(true);

                                // UserRepository
                                Class<?> fieldType = field.getType();

                                // UserRepository -> instance
                                Object instanceToInject = applicationContext.get(fieldType);

                                try {
                                    // UserService
                                    //  - UserRepository userRepository

                                    // 4. inject UserRepository into the field
                                    field.set(instance, instanceToInject);
                                } catch (IllegalAccessException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                });
    }

    @SneakyThrows
    private void injectValueAnnotation() {
        Properties properties = new Properties();
        // read file inside *.jar
        properties.load(Runner.class.getClassLoader().getResourceAsStream("application.properties"));

        applicationContext.entrySet()
                .stream()
                .forEach(service -> { // UserService, instance
                    Arrays.stream(service.getKey().getDeclaredFields())
                            .filter(field -> field.isAnnotationPresent(Value.class))
                            .forEach(fieldWithValue -> {
                                fieldWithValue.setAccessible(true);
                                Value valueAnnotation = fieldWithValue.getAnnotation(Value.class);

                                // file.path
                                String dataFromProperties = properties.getProperty(valueAnnotation.value());
                                if (dataFromProperties == null) {
                                    return;
                                }

                                try {
                                    fieldWithValue.set(service.getValue(), dataFromProperties);
                                } catch (IllegalAccessException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                });
    }

    public <T> T getService(Class<T> classType) {
        return (T) applicationContext.get(classType);
    }
}
