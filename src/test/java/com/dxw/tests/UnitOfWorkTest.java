package com.dxw.tests;

import com.dxw.common.services.ServiceException;
import com.dxw.flfs.data.HibernateService;
import com.dxw.flfs.data.HibernateServiceImpl;
import com.dxw.flfs.data.dal.DefaultGenericRepository;
import com.dxw.flfs.data.dal.UnitOfWork;
import com.dxw.flfs.data.models.AppConfig;
import org.junit.*;

import java.util.Collection;
import java.util.Optional;

/**
 * Created by zhang on 2016-04-28.
 */
public class UnitOfWorkTest {
    private static HibernateService hibernateService;

    @BeforeClass
    public static void setUpClass() throws ServiceException {
        hibernateService = new HibernateServiceImpl();
        hibernateService.init();


    }

    @AfterClass
    public static void tearDownClass() {
        try {
            hibernateService.destroy();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }


    @Before
    public void setUp() throws ServiceException {
    }

    @After
    public void tearDown() throws ServiceException {

    }

    @Test
    public void loadAppConfig(){
        String appId = "93876c73-64d7-4f46-a422-6b16d25b5a43";
        try(UnitOfWork uow = new UnitOfWork(hibernateService.getSession())) {
            DefaultGenericRepository<AppConfig, Long> r = uow.getAppConfigRepository();
            Collection<AppConfig> configs = r.findAll();

            Optional<AppConfig> config = configs.stream()
                    .filter(c-> c.getAppId().equals(appId))
                    .findFirst();
            /* AppConfig config = r.find(appId); */

            if( config.isPresent())
                System.out.println(config.get().getAppId());

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
