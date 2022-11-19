package com.openclassrooms.go4lunch;

import static com.openclassrooms.go4lunch.RestaurantTest.*;
import static com.openclassrooms.go4lunch.WorkmateTest.*;

import static junit.framework.Assert.assertEquals;

import com.openclassrooms.go4lunch.models.Lunch;
import com.openclassrooms.go4lunch.models.Restaurant;
import com.openclassrooms.go4lunch.models.Workmate;

import org.junit.Before;
import org.junit.Test;

public class LunchTest {
    private Lunch lunchToTest;
    static String dateLunchToTest="Fri Aug 19 13:00:00 GMT 2022";
    static Restaurant restaurantChoosedToTest= new Restaurant(idRestaurantToTest,nameRestaurantToTest,phoneRestaurantToTest,
            ratingRestaurantToTest,typeRestaurantToTest,urlPictureRestaurantToTest,webSiteRestaurantToTest,adressRestaurantToTest,
            hourClosedRestaurantToTest);
    static Workmate workmateToTest=new Workmate(idWorkmateToTest, nameWorkmateToTest, emailWorkmateToTest,urlPictureWorkmateToTest);
    static String dateLunchToTest2="Mon Aug 15 13:00:00 GMT 2022";
    static Restaurant restaurantChoosedToTest2=new Restaurant(idRestaurantToTest2,nameRestaurantToTest2,phoneRestaurantToTest2,
            ratingRestaurantToTest2,typeRestaurantToTest2,urlPictureRestaurantToTest2,webSiteRestaurantToTest2,adressRestaurantToTest2,
            hourClosedRestaurantToTest2);
    static Workmate workmateToTest2=new Workmate(idWorkmateToTest2, nameWorkmateToTest2, emailWorkmateToTest2,urlPictureWorkmateToTest2);

    @Before
    public void setUp() {
        lunchToTest = new Lunch(dateLunchToTest,restaurantChoosedToTest,workmateToTest);
    }
    @Test
    public void getDateLunch() {
        assertEquals(dateLunchToTest, lunchToTest.getDateLunch());
    }
    @Test
    public void getRestaurantChoosed() {
        assertEquals(restaurantChoosedToTest, lunchToTest.getRestaurantChoosed());
    }
    @Test
    public void getWorkmates() {
        assertEquals(workmateToTest, lunchToTest.getWorkmates());
    }

    @Test
    public void setDateLunch() {
        lunchToTest.setDateLunch(dateLunchToTest2);
        assertEquals(dateLunchToTest2, lunchToTest.getDateLunch());
    }
    @Test
    public void setRestaurantChoosed() {
        lunchToTest.setRestaurantChoosed(restaurantChoosedToTest2);
        assertEquals(restaurantChoosedToTest2, lunchToTest.getRestaurantChoosed());
    }
    @Test
    public void setWorkmates() {
        lunchToTest.setWorkmates(workmateToTest2);
        assertEquals(workmateToTest2, lunchToTest.getWorkmates());
    }
}
