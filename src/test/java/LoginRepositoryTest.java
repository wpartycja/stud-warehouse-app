import edu.pw.pap21z.z15.db.LoginRepository;
import edu.pw.pap21z.z15.db.model.Account;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.EntityManager;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LoginRepositoryTest {

    private static LoginRepository repo;

    @BeforeAll
    static void init() {
        // create user for testing
        Account testUser = new Account();
        testUser.setId("testlogin");
        testUser.setName("TestName");
        testUser.setName("TestSurname");
        testUser.setPassword(BCrypt.hashpw("haslo", BCrypt.gensalt()));

        // mock databse so that testuser can be retrieved
        var sessionMock = mock(EntityManager.class);
        when(sessionMock.find(Account.class, "testlogin")).thenReturn(testUser);

        repo = new LoginRepository(sessionMock);
    }

    @Test
    void shouldGetAccountByUsername() {
        var testuser = repo.getAccountByUsername("testlogin");
        Assertions.assertThat(testuser).isNotNull();
        Assertions.assertThat(testuser.getId()).isEqualTo("testlogin");
    }

    @Test
    void shouldCheckCredentials() {
        var wrongUsername = repo.checkCredentials("noexist_login", "password");
        Assertions.assertThat(wrongUsername).isFalse();

        var wrongPassword = repo.checkCredentials("testlogin", "alamakota");
        Assertions.assertThat(wrongPassword).isFalse();

        var correctCredentials = repo.checkCredentials("testlogin", "haslo");
        Assertions.assertThat(correctCredentials).isTrue();
    }


}