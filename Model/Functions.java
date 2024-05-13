package Project.Model;

public class Functions {

    public static int CaclulatePriority(int NumberOfPeople,int Situation){
        if(NumberOfPeople>3) return Situation+1;
        return Situation;
    }

    public static boolean checkPhone(String Phone){
        return (!Phone.equals("enter number:") && Phone.length()==10 && Phone.charAt(0)=='0'&& Phone.charAt(1)=='5');
    }

    public static boolean currectOldID(String ID){
        return (ID.length()==9 && DBfunctions.isVolunteerInDB(ID));
    }
    
    public static boolean currectNewID(String ID){
        return (!ID.equals("enter number:") && ID.length()==9 && !DBfunctions.isVolunteerInDB(ID));
    }        
}
