public class Runner {
    public static void main(String[] args) {
        SIS sis = new SIS();
        System.out.println(sis.getGrade(3664135,5710453,20212));
//        sis.updateExam(3664135,5710453,"Final",100);
        System.out.println(sis.getGrade(3664135,5710453,20212));
//        sis.findCourse(5710499);
//        sis.createHistogram(5710453,20102);
//        sis.createTranscript(8200806);
//        sis.createTranscript(2171825);
        System.out.println(sis.getGrade(2171825,5710453,20202));


    }
}
