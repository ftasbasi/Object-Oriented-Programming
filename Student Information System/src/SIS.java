import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class SIS {
    private static String fileSep = File.separator;
    private static String lineSep = System.lineSeparator();
    private static String space   = " ";

    private List<Student> studentList = new ArrayList<>();

    public SIS(){ processOptics(); }

    private void processOptics(){
        // TODO

        //FILE OPERATIONS
        List<Path> pathList=new ArrayList<>();
        final String dir=System.getProperty("user.dir");
        try (Stream<Path> paths = Files.walk(Paths.get("input"))) {
            pathList= paths
                    .filter(Files::isRegularFile)
                    .filter(p-> p.toString().endsWith(".txt")).collect(Collectors.toList());


        } catch (Exception e) {
            e.printStackTrace();
        }
        //CONSUMER FOR OPTIC READING
        Consumer<Path> read_optic=fileIn->{
            try {
                Stream<String> singleFileStream=Files.lines(Paths.get(String.valueOf(fileIn)));
                List<List<String>> studentUnparsed=singleFileStream.map(unparsedLine-> Arrays.asList(unparsedLine.split(" "))).collect(Collectors.toList());

                //GETTING STUDENTS, COURSES, EXAMS, OPTIC_FORMS
                List<String> studentIDList=studentUnparsed.get(0);
                List<String> studentSemester_Course=studentUnparsed.get(1);
                List<String> studentExamType=studentUnparsed.get(2);
                List<String> studentOpticForm=studentUnparsed.get(3);

                //ADDITIONAL PARSE OPERATIONS
                long studentIDList_param_count = studentIDList.stream().count();
                String studentNameSurname= studentIDList.stream().map(s -> s.replaceAll("[0-9]", "")).collect(Collectors.joining(" "));
                String studentIDNumber= studentIDList.stream().skip(studentIDList_param_count - 1).findFirst().get();
                String semester=studentSemester_Course.stream().findFirst().get();
                String course=studentSemester_Course.stream().skip(1).findFirst().get();
                String credit=studentSemester_Course.stream().reduce((first, second) -> second).get();
                String examType=studentExamType.get(0);
                String opticForm=studentOpticForm.get(0);

                //GETTING GRADE FOR SPECIFIC EXAM
                int questionCount= studentOpticForm.get(0).length();
                int trueCount= opticForm.chars().reduce(0, (a, c) -> a + (c == 'T' ? 1 : 0));
                double grade=((double) trueCount/(double)questionCount)*100;

                //SAVING COURSES
                Course course_in=new Course(Integer.parseInt(course),Integer.parseInt(semester),examType,Integer.parseInt(credit),grade);

                //SAVING STUDENTS
                Optional<Student> curr_student= studentList.stream().filter(student -> student.getStudentID()==Integer.parseInt(studentIDNumber)).findFirst();

                if (!curr_student.isPresent()){

                    //CREATE NEW STUDENT IF NOT EXIST, THEN ADD COURSE

                    String[] name_surname= studentNameSurname.split(" ");
                    String student_surname=name_surname[name_surname.length-1];
                    String[] student_names=Arrays.copyOf(name_surname,name_surname.length-1);
                    Student new_student=new Student(student_names,student_surname,Integer.parseInt(studentIDNumber));
                    new_student.getTakenCourses().add(course_in);
                    this.studentList.add(new_student);
                }else {

                    //IF EXIST SAVE STUDENT'S NEW COURSE
                    curr_student.get().getTakenCourses().add(course_in);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        //READ WHOLE INPUT USING CONSUMER
        pathList.stream().forEach(read_optic);

        //findCourse(5710443);
        //createHistogram(5710443,20201);
        //createTranscript(2041853);
   /*     System.out.println(getGrade(2041853,9999991,20192));
        System.out.println(getGrade(2041853,9999991,20201));
        System.out.println(getGrade(2041853,9999991,20211));
        updateExam(2041853,9999991,"Final",10.0);
        System.out.println(getGrade(2041853,9999991,20192));
        System.out.println(getGrade(2041853,9999991,20201));
        System.out.println(getGrade(2041853,9999991,20211));

    */
        //updateExam(2041853,9999991,"Final",30.0);
        //System.out.println(getGrade(2041853,9999991,20201));
        // createTranscript(2041853);
    }

    public double getGrade(int studentID, int courseCode, int year){
        // TODO

        Optional<Student> student= studentList.stream().filter(s -> s.getStudentID()==studentID).findFirst();
        double retVal=0;
        if (student.isPresent()){
            double midterm_points=student.get().getTakenCourses().stream().filter(course -> course.getCourseCode()==courseCode).filter(course -> course.getYear()==year).filter(course -> course.getExamType().contains("Midterm")).collect(Collectors.averagingDouble(Course::getGrade));
            double final_point=student.get().getTakenCourses().stream().filter(course -> course.getCourseCode()==courseCode).filter(course -> course.getYear()==year).filter(course -> course.getExamType().contains("Final")).findFirst().get().getGrade();
            retVal=midterm_points*0.5+final_point*0.5;
        }
        return Math.round(retVal*100.0)/100.0;
    }

    public void updateExam(int studentID, int courseCode, String examType, double newGrade){
        // TODO

        //GET STUDENT
        Optional<Student> student= studentList.stream().filter(s -> s.getStudentID()==studentID).findFirst();

        if (student.isPresent()){
            //IF EXIST, THEN GET COURSES
            List<Course> course= student.get().getTakenCourses().stream().filter(c -> c.getCourseCode()==courseCode).filter(c -> c.getExamType().contains(examType)).collect(toList());
            //ENSURE ITS LAST TAKEN COURSE
            Course course_specific = student.get().getTakenCourses().stream().filter(c-> c.getCourseCode() == courseCode && c.getExamType().contains(examType)).sorted(Comparator.comparingInt(Course::getYear).reversed())
                    .findFirst().get();
            int lastyear=course_specific.getYear();
            if (course.size()>0){
                //SET NEW GRADE
                course.stream().filter(c->c.getYear()==lastyear).forEach(s->s.setGrade(newGrade));

            }
        }
    }

    public void createTranscript(int studentID){
        // TODO
        //FIND STUDENT
        Optional<Student> student= studentList.stream().filter(s -> s.getStudentID()==studentID).findFirst();
        if (student.isPresent()){
            //GET STUDENT'S ALL COURSES AS A LIST OF COURSE LIST

            //FLATTEN THE LIST IN TO 1D
            List<Course> flatList =
                    student.get().getTakenCourses().stream()
                            .collect(Collectors.toList());

            //COMPARATORS AND SUPPLIER FOR YEAR AND COURSE_CODE INFO
            Comparator<Course> byYear = Comparator.comparingInt(Course::getYear);
            Comparator<Course> byCourseCode = Comparator.comparingInt(Course::getCourseCode);
            Comparator<Course> byYear_byCourseCode = byYear.thenComparing(byCourseCode);

            //GRADE TO LETTER TABLE
            TreeMap<Integer, String> gradeMap = new TreeMap<>();
            gradeMap.put(90, "AA");
            gradeMap.put(85, "BA");
            gradeMap.put(80, "BB");
            gradeMap.put(75, "CB");
            gradeMap.put(70, "CC");
            gradeMap.put(65, "DC");
            gradeMap.put(60, "DD");
            gradeMap.put(55, "FD");
            gradeMap.put(0, "FF");

            //LETTER TO CREDIT TABLE
            TreeMap<String,Double> creditMap = new TreeMap<>();
            creditMap.put("AA",4.0);
            creditMap.put("BA",3.5);
            creditMap.put("BB",3.0);
            creditMap.put("CB",2.5);
            creditMap.put("CC",2.0);
            creditMap.put("DC",1.5);
            creditMap.put("DD",1.0);
            creditMap.put("FD",0.5);
            creditMap.put("FF",0.0);

            //COURSE LIST SORTED WITH COMPARATOR DEFINED ABOVE (YEAR AND COURSE_CODE TOGETHER)
            Collection<List<Course>> listOfyears =
                    flatList.stream().sorted(byYear_byCourseCode)
                            .collect(groupingBy(el->el.getYear()))
                            .values();

            //SET OF SEMESTERS
            Set<String> hash_Set
                    = new LinkedHashSet<>();
            listOfyears.stream().sorted(Comparator.comparing(x->x.get(0).getYear())).peek(semester->hash_Set.add(String.valueOf(semester.get(0).getYear()))).forEach(xx->xx.stream().forEach(c->hash_Set.add(c.getCourseCode()+" "+gradeMap.floorEntry((int) Math.round(getGrade(studentID,c.getCourseCode(),c.getYear()))).getValue())));


            hash_Set.forEach(System.out::println);


            //TOTAL COURSE CREDITS VS GAINED CREDITS BY STUDENT
            Student man=studentList.stream()
                    .filter(s->s.getStudentID()==studentID).findFirst().get();

            //GETTING SUM OF CREDITS FOR TAKEN COURSES
            double totalCredit = man.getTakenCourses().stream()
                    .filter(crs -> man.getTakenCourses().stream().filter(coursex -> crs.getCourseCode() == coursex.getCourseCode())
                            .filter(coursex -> coursex.getYear() != crs.getYear())
                            .allMatch(coursex->crs.getYear() > coursex.getYear())
                    ).mapToDouble(c-> c.getCredit()).sum();

            //GETTING SUM OF GAINED CREDITS FOR TAKEN COURSES
            double gainedCredit = man.getTakenCourses().stream()
                    .filter(crs -> man.getTakenCourses().stream().filter(coursex -> crs.getCourseCode() == coursex.getCourseCode())
                            .filter(coursex -> coursex.getYear() != crs.getYear())
                            .allMatch(coursex->crs.getYear() > coursex.getYear())
                    ).mapToDouble(c-> creditMap.floorEntry(gradeMap.floorEntry((int) Math.round(getGrade(studentID,c.getCourseCode(),c.getYear()))).getValue()).getValue()).sum();

            //SINCE WE HAVE DUPLICATE STREAM
            totalCredit/=6;

            gainedCredit/=2;

            System.out.println("CGPA: "+Math.round(gainedCredit/totalCredit*100.0)/100.0);
        }
    }

    public void findCourse(int courseCode){
        // TODO

        //GETTING COURSES AS LIST OF COURSE LIST
        List<List<Course>> listoflists=studentList.stream()
                .map(Student::getTakenCourses)
                .collect(Collectors.toList());

        //FLATTENING
        List<Course> flatList =
                listoflists.stream()
                        .flatMap(List::stream)
                        //.sorted(Comparator.comparingInt(Course::getYear))
                        .filter(c->c.getCourseCode()==courseCode)
                        .collect(Collectors.toList());
        //GETTING COURSES BY YEARS
        List<Integer> year_list=flatList.stream()
                .map(s -> s.getYear())
                .collect(Collectors.toList());

        //MAPPING COURSE CODE (AS INTEGER) AND ENROLLED STUDENT COUNTS
        Map<Integer, Long> map_trtrt=year_list.stream()
                .collect(groupingBy(Integer::intValue, counting()));


        map_trtrt.entrySet().stream().distinct().sorted(Map.Entry.comparingByKey()).forEach((k)-> System.out.println(k.getKey()+" "+k.getValue()/6));
    }

    public void createHistogram(int courseCode, int year){
        // TODO

        //TAKING STUDENT THAT TOOK THIS COURSE
        List<Student> who_took=studentList.stream()
                .filter(s->s.getTakenCourses().stream().filter(c->c.getCourseCode()==courseCode && c.getYear()==year).findFirst().isPresent())
                .collect(Collectors.toList());

        //FILTER FOR DISTINCT VALUES
        who_took.stream().forEach(x->x.getTakenCourses().stream().filter(c->c.getCourseCode()==courseCode).distinct().collect(Collectors.toList()));
        List<Integer> list_of_students= who_took.stream().map(s->s.getStudentID()).collect(toList());

        //GET GRADES OF ALL STUDENTS
        List<Double> grades=list_of_students.stream()
                .map(s->getGrade(s.intValue(),courseCode,year))
                .collect(toList());

        //GETTING ALL RANGES ONE BY ONE
        int first= (int) grades.stream().filter(x->x.doubleValue()>=0).filter(x->x.doubleValue()<10).count();
        int second= (int) grades.stream().filter(x->x.doubleValue()>=10).filter(x->x.doubleValue()<20).count();
        int third= (int) grades.stream().filter(x->x.doubleValue()>=20).filter(x->x.doubleValue()<30).count();
        int forth= (int) grades.stream().filter(x->x.doubleValue()>=30).filter(x->x.doubleValue()<40).count();
        int fifth= (int) grades.stream().filter(x->x.doubleValue()>=40).filter(x->x.doubleValue()<50).count();
        int sixth= (int) grades.stream().filter(x->x.doubleValue()>=50).filter(x->x.doubleValue()<60).count();
        int seventh= (int) grades.stream().filter(x->x.doubleValue()>=60).filter(x->x.doubleValue()<70).count();
        int eighth= (int) grades.stream().filter(x->x.doubleValue()>=70).filter(x->x.doubleValue()<80).count();
        int ninth= (int) grades.stream().filter(x->x.doubleValue()>=80).filter(x->x.doubleValue()<90).count();
        int tenth= (int) grades.stream().filter(x->x.doubleValue()>=90).filter(x->x.doubleValue()<=100).count();


        //OUTPUT RANGES-COUNTS
        System.out.println("0-10 "+first);
        System.out.println("10-20 "+second);
        System.out.println("20-30 "+third);
        System.out.println("30-40 "+forth);
        System.out.println("40-50 "+fifth);
        System.out.println("50-60 "+sixth);
        System.out.println("60-70 "+seventh);
        System.out.println("70-80 "+eighth);
        System.out.println("80-90 "+ninth);
        System.out.println("90-100 "+tenth);


    }
}