package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@DisplayName("사용자 데이터")
public class UserTest {

    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private UserRepository userRepository;

    static Stream<Arguments> example() {
        return Stream.of(Arguments.of(JAVAJIGI), Arguments.of(SANJIGI));
    }

    @ParameterizedTest
    @DisplayName("저장")
    @MethodSource("example")
    void save(User user) {
        //given, when
        User actual = userRepository.save(user);

        //then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getCreatedAt()).isNotNull(),
            () -> assertThat(actual.getName()).isEqualTo(user.getName()),
            () -> assertThat(actual.getUserId()).isEqualTo(user.getUserId())
        );
    }

    @ParameterizedTest
    @DisplayName("아이디로 검색")
    @MethodSource("example")
    void findByUserId(User user) {
        //given
        User expected = userRepository.save(user);

        //when
        User actual = userByUserId(user.getUserId());

        //then
        assertThat(actual).isEqualTo(expected);
    }

    private User userByUserId(String userId) {
        return userRepository.findByUserId(userId)
            .orElseThrow(() -> new EntityNotFoundException(String.format("%s is not found", userId)));
    }
}
