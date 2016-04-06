package ca.douglascollege.flamingdodos.realestate.forms;

import ca.douglascollege.flamingdodos.realestate.data.models.PropertyListingModel;
import org.junit.BeforeClass;

public class NewPropertyListingFormTest extends BaseFormTest<NewPropertyListing> {
    @BeforeClass
    public static void setUpClass() {
        addFieldsFromClass(NewPropertyListing.class);
    }

    @Override
    protected NewPropertyListing createFormInstance() {
        return new NewPropertyListing(null, new NewPropertyListing.AddListingCallback() {
            @Override
            public void addListing(PropertyListingModel listing) {

            }
        });
    }
}
