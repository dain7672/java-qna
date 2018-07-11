package codesquad.web.util;

import codesquad.web.domain.*;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class RepositoryUtil {

    public static Question findQuestionById(long id, QuestionRepository qr){
        return qr.findById(id).get();
    }

    public static User findUserById(long id, UserRepository ur){
        return ur.findById(id).get();
    }

    public static List<Answer> findAnswersByQuestionId(long questionId, AnswerRepository ar) {
        return ar.findByQuestion_Id(questionId)
                .stream()
                .filter(answer -> !answer.isDeleted())
                .collect(Collectors.toList());
    }
    public static Answer findAnswerById(long id, AnswerRepository ar) {
        return ar.findById(id).get();
    }

    public static void setDeleted(AnswerRepository repository, Long answerId) {
        Answer answer = findAnswerById(answerId, repository);
        answer.setDeleted(true);
        repository.save(answer);
    }

    public static List<Question> findQuestions(QuestionRepository qr) {
        return StreamSupport.stream(qr.findAll().spliterator(), false)
                .filter(question -> !question.isDeleted())
                .collect(Collectors.toList());

    }
}
