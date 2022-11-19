package com.openclassrooms.go4lunch;
import static junit.framework.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import com.openclassrooms.go4lunch.models.Workmate;

public class WorkmateTest {
    private Workmate workmateToTest;
    static String idWorkmateToTest="1237";
    static String nameWorkmateToTest="salah";
    static String emailWorkmateToTest="salah@gmail.com";
    static String urlPictureWorkmateToTest="url";
    static Boolean isNotificationActiveWorkmateToTest=true;
    static String idWorkmateToTest2="2345";
    static String nameWorkmateToTest2="salah2";
    static String emailWorkmateToTest2="salah2@yahoo.fr";
    static String urlPictureWorkmateToTest2="url2";

    @Before
    public void setUp() {
        workmateToTest = new Workmate(idWorkmateToTest, nameWorkmateToTest, emailWorkmateToTest,urlPictureWorkmateToTest,isNotificationActiveWorkmateToTest);
    }
    @Test
    public void getIdWorkmate() {
        assertEquals(idWorkmateToTest, workmateToTest.getIdWorkmate());
    }
    @Test
    public void getNameWorkmate() {
        assertEquals(nameWorkmateToTest, workmateToTest.getName());
    }
    @Test
    public void getEmailWorkmate() {
        assertEquals(emailWorkmateToTest, workmateToTest.getEmail());
    }
    @Test
    public void getUrlPictureWorkmate() {
        assertEquals(urlPictureWorkmateToTest, workmateToTest.getUrlPicture());
    }
    @Test
    public void setIdWorkmate() {
        workmateToTest.setIdWorkmate(idWorkmateToTest2);
        assertEquals(idWorkmateToTest2, workmateToTest.getIdWorkmate());
    }
    @Test
    public void setNameWorkmate() {
        workmateToTest.setName(nameWorkmateToTest2);
        assertEquals(nameWorkmateToTest2, workmateToTest.getName());
    }
    @Test
    public void setEmailWorkmate() {
        workmateToTest.setEmail(emailWorkmateToTest2);
        assertEquals(emailWorkmateToTest2, workmateToTest.getEmail());
    }
    @Test
    public void setUrlPictureWorkmate() {
        workmateToTest.setUrlPicture(urlPictureWorkmateToTest2);
        assertEquals(urlPictureWorkmateToTest2, workmateToTest.getUrlPicture());
    }
}