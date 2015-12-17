import com.devrajs.practice.oyo.HospitalGetter;
import com.devrajs.practice.oyo.entity.Hospital;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Created by devraj.singh on 10/23/15.
 */
public class TestClass {

    @Test
    public void testHospitalFinderAllHospital() {
        try {
            HospitalGetter hospitalGetter = new HospitalGetter();
            List<Hospital> list = HospitalGetter.findAllHospitalForGivenLocation(-33.8670, 151.1957, 3000);
            for (Hospital hospital : list) {
                System.out.println(hospital.toString());
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void findNearest()
    {
        try
        {
            System.out.println(HospitalGetter.getNearestHospitalDistance(-33.8670, 151.1957));
        }
        catch (Exception e)
        {
            Assert.fail(e.getMessage());
        }
    }
}
