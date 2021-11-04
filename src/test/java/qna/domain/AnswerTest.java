package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Stream;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@DisplayName("답변 데이터")
public class AnswerTest {

    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answerRepository;

    static Stream<Arguments> example() {
        return Stream.of(Arguments.of(A1), Arguments.of(A2));
    }

    @ParameterizedTest
    @DisplayName("저장")
    @MethodSource("example")
    void save(Answer answer) {
        //when
        Answer actual = answerRepository.save(answer);

        //then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getCreatedAt()).isNotNull(),
            () -> assertThat(actual.getContents()).isEqualTo(answer.getContents()),
            () -> assertThat(actual.getQuestionId()).isEqualTo(answer.getQuestionId()),
            () -> assertThat(actual.getWriterId()).isEqualTo(answer.getWriterId())
        );
    }

    @ParameterizedTest
    @DisplayName("아이디로 검색")
    @MethodSource("example")
    void findByIdAndDeletedFalse(Answer answer) {
        //given
        Answer expected = answerRepository.save(answer);

        //when
        Answer actual = answerById(expected.getId());

        //then
        assertThat(actual)
            .isEqualTo(expected);
    }

    @ParameterizedTest
    @DisplayName("질문 아이디로 검색")
    @MethodSource("example")
    void findByQuestionIdAndDeletedFalse(Answer answer) {
        //given
        Answer expected = answerRepository.save(answer);

        // when
        List<Answer> actual = answerRepository.findByQuestionIdAndDeletedFalse(
            answer.getQuestionId());

        //then
        assertThat(actual).contains(expected);
    }

    private Answer answerById(Long id) {
        return answerRepository.findByIdAndDeletedFalse(id)
            .orElseThrow(() -> new EntityNotFoundException(String.format("id(%s) is not found", id)));
    }
}
