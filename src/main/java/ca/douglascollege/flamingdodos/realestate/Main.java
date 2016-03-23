package ca.douglascollege.flamingdodos.realestate;

import ca.douglascollege.flamingdodos.realestate.forms.Home;
import org.tmatesoft.sqljet.core.SqlJetException;

public class Main {

    public static void main(String args[]) throws SqlJetException {
        new Home().open();
    }

}
