package info.vadzimko.engine;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

public class Game {

    private static final Long GAME_DURATION_MILLIS = 90000L;

    private boolean started;
    private boolean finished;
    private long beginTime;
    private long duration;

    private String lastWinner = "Albert Einstein";
    private List<Attempt> lastAnswers = new ArrayList<>(Arrays.asList(
            new Attempt("Donald Trump", 1L),
            new Attempt("Vladimir Putin", 1L),
            new Attempt("Albert Einstein", 3L)));

    private Map<String, Long> answers = new HashMap<>();
    private Map<String, String> names = new HashMap<>();



    public Game() {
        started = false;
        finished = true;
    }

    public boolean startNewGame(Long duration) {
        if (!finished) {
            return false;
        }

        this.beginTime = System.currentTimeMillis();
        this.duration = duration;
        started = true;
        finished = false;

        Thread finish = new Thread(() -> {
            try {
                Thread.sleep(duration);
            } catch (InterruptedException interrupted) {
                Thread.currentThread().interrupt();
            }
            finishGame();
        });
        finish.start();

        return true;
    }

    private void finishGame() {
        finished = true;
        started = false;

        List<Attempt> newAnswers = new ArrayList<>();
        for (Map.Entry<String, Long> entry : answers.entrySet()) {
            newAnswers.add(new Attempt(names.get(entry.getKey()), entry.getValue()));
        }
        newAnswers.sort((lhs, rhs) -> lhs.number < rhs.number ? -1 : (lhs.number > rhs.number) ? 1 : 0);
        lastAnswers = newAnswers;

        int index = 1;
        while (index < lastAnswers.size() && lastAnswers.get(index - 1).number.equals(lastAnswers.get(index).number)) {
            while (index < lastAnswers.size() && lastAnswers.get(index - 1).number.equals(lastAnswers.get(index).number)) {
                index++;
            }
            index++;
        }

        if (index > lastAnswers.size()) {
            index = lastAnswers.size();
        }

        if (index == lastAnswers.size() && index > 1) {
            if (!lastAnswers.get(index - 1).number.equals(lastAnswers.get(index - 2).number)) {
                lastWinner = lastAnswers.get(index - 1).name;
            } else {
                lastWinner = "Nobody";
            }
        } else {
            lastWinner = lastAnswers.get(index - 1).name;
        }
        answers = new HashMap<>();
        names = new HashMap<>();
    }

    public boolean isStarted() {
        return started;
    }

    public long timeToEndInSeconds() {
        return (beginTime + duration - System.currentTimeMillis()) / 1000;
    }

    public Map<String, Long> getAnswers() {
        return answers;
    }

    public String getLastWinner() {
        return lastWinner;
    }

    public void addAnswer(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Long number = Long.parseLong(request.getParameter("answer"));

        if (number < 1) {
            return;
        }

        if (!getAnswers().containsKey((String) session.getAttribute("UserID"))) {
            getAnswers().put((String) session.getAttribute("UserID"), number);
            session.setAttribute("Number", number);
            names.put((String) session.getAttribute("UserID"), (String) session.getAttribute("Name"));

            if (!isStarted()) {
                startNewGame(GAME_DURATION_MILLIS);
            }
        }
    }

    public static class Attempt {
        private String name;
        private Long number;

        public Attempt(String name, Long number) {
            this.name = name;
            this.number = number;
        }

        public String getName() {
            return name;
        }

        public Long getNumber() {
            return number;
        }
    }

    public List<Attempt> getLastAnswers() {
        return lastAnswers;
    }
    public Map<String, String> getNames() {
        return names;
    }

}
