package MonitoredData;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class Operation {

    private List<MonitoredData> readDateFromFile(){//task 0
        try {
            return Files.lines(Paths.get("Activity.txt"))
                    .map(line -> line.split("\t\t")).map(s -> new MonitoredData(s[0], s[1], s[2]))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public long countDifferentData(){//task 1
        return readDateFromFile().stream()
                .map(MonitoredData::getStartTime).map(line->line.split(" ")).map(s->s[0])
                .distinct()
                .count();
    }


    private Map<String,Long> actionType(){//task 2
//        return readDateFromFile().stream().collect(Collectors.toMap(MonitoredData::getActivity,counter->1,Integer::sum));
        return readDateFromFile().stream()
                .collect(groupingBy(MonitoredData::getActivity,Collectors.counting()));
    }


    public void writeInfoAboutActivity1(){//task 2
        Path path = Paths.get("task2.txt");
        try {
            Files.write(path, ()->actionType().entrySet().stream().<CharSequence>map(p->p.getKey() + " " + p.getValue()).iterator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private Map<Integer, Map<String, Long>> activityForEachDay(){//task 3
        return readDateFromFile().stream()
                .collect(groupingBy(s->Integer.parseInt(s.getStartTime().substring(8,10)),groupingBy(MonitoredData::getActivity,Collectors.counting())));
    }

    public void writeInfoAboutActivity2(){//task 3
        Path path = Paths.get("task3.txt");
        try {
            Files.write(path, ()->activityForEachDay().entrySet().stream().<CharSequence>map(p->p.getKey()+ " "+p.getValue()).iterator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private Map<String,Long> activities10Hours(){//task 4
        DateTimeFormatter formatter =DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm:ss");
        return readDateFromFile().stream()
                .collect(groupingBy(p->p.getActivity(),Collectors.summingLong(y->Duration.between(LocalDateTime.parse(y.getStartTime(),formatter),LocalDateTime.parse(y.getEndTime(),formatter)).getSeconds())))
                .entrySet()
                .stream()
                .filter(q->q.getValue()>10*3600)
                .collect(Collectors.toMap(q->q.getKey(),q->q.getValue()/3600));
    }

    public void writeInfoAboutActivity3(){//task 4
        Path path = Paths.get("task4.txt");
        try {
            Files.write(path, ()->activities10Hours().entrySet().stream().<CharSequence>map(p->p.getKey() + " " + p.getValue()).iterator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private List<String> lessThan5Min(){//task 5
        DateTimeFormatter formatter =DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm:ss");
        return readDateFromFile().stream().filter((MonitoredData a)->Duration.between(LocalDateTime.parse(a.getStartTime(),formatter),LocalDateTime.parse(a.getEndTime(),formatter)).getSeconds()<5*60)                  .collect(groupingBy(a->a.getActivity(),Collectors.counting()))
                .entrySet().stream()
                .filter(p->p.getValue()/actionType().get(p.getKey())>0.9)
                .map(p->p.getKey()).collect(toList());
    }

    public void writeInfoAboutActivity4(){//task 5
        Path path = Paths.get("task5.txt");
        try {
            Files.write(path,lessThan5Min(), Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
