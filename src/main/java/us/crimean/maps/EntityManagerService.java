package us.crimean.maps;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerService {
    private static final EntityManagerFactory emfInstance = Persistence
            .createEntityManagerFactory("transactions-optional");

    private EntityManagerService() {
    }

    public static EntityManagerFactory get() {
        return emfInstance;
    }
}