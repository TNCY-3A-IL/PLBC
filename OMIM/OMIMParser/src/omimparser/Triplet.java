/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package omimparser;

/**
 *
 * @author Fabien
 */
public class Triplet {
    private String object;
    private String property;
    private String subject;

    public Triplet(String object, String property, String subject) {
        this.object = object;
        this.property = property;
        this.subject = subject;
    }

    public String getObject() {
        return object;
    }

    public String getProperty() {
        return property;
    }

    public String getSubject() {
        return subject;
    }
    
    @Override
    public String toString(){
        return object + " " + property + " " + subject + ".";
    }
}
