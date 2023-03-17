package bg.sofia.uni.fmi.mjt.splitwise;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static org.junit.jupiter.api.Assertions.*;

class ServerCommandExecutorTest {

    private static Storage storage;
    private static ServerCommandExecutor serverCommandExecutor;
    private static String newFileName = "testCaseFileNameForWriting.txt";

    @BeforeEach
    void setUp() throws IOException {
        storage = new Storage();
        storage.setFileName(newFileName);
        new File(newFileName).createNewFile();
        BufferedWriter bfw = Files.newBufferedWriter(Path.of(newFileName), StandardOpenOption.TRUNCATE_EXISTING);
        String str = "u5; p5; ; ; ; ; \n" +
                "u1; p1; u2, u3, u4, ; 6.25, 1.25, 1.25, ; u2, ; gg, \n" +
                "u2; p2; u1, u3, u4, ; -6.25, 0.0, 0.0, ; u1, ; gg, \n" +
                "u3; p3; u1, u2, u4, ; -1.25, 0.0, 0.0, ; ; gg, \n" +
                "u4; p4; u1, u2, u3, ; -1.25, 0.0, 0.0, ; ; gg, \n";
        bfw.write(str);
        bfw.flush();
        bfw.close();
        storage.loadDataFromFile();

        serverCommandExecutor = new ServerCommandExecutor(storage);
    }

    @Test
    void testExecute() {
    }

    @Test
    void testRegisterNewUser() throws IOException {
        String[] command = new String[3];
        command[0] = "register";
        command[1] = "newUser newPassword";
        command[2] = "1";
        String response = serverCommandExecutor.execute(command);
        assertEquals("Successfully registered user newUser.", response);
        Files.deleteIfExists(Path.of("testCaseFileNameForWriting.txt"));
    }

    @Test
    void testLoginExistingUser() throws IOException {
        String[] command = new String[3];
        command[0] = "login";
        command[1] = "u1 p1";
        command[2] = "1";
        String response = serverCommandExecutor.execute(command);
        assertEquals("Successfully logged in as user u1.", response);
        Files.deleteIfExists(Path.of("testCaseFileNameForWriting.txt"));
    }

    @Test
    void testAddFriend() throws IOException {
        String[] command = new String[3];
        command[0] = "login";
        command[1] = "u1 p1";
        command[2] = "1";
        String response = serverCommandExecutor.execute(command);
        assertEquals("Successfully logged in as user u1.", response);
        String[] command2 = new String[3];
        command2[0] = "add-friend";
        command2[1] = "u5";
        command2[2] = "1";
        String response2 = serverCommandExecutor.execute(command2);
        assertEquals("u1 successfully created a balance with user u5 and created a friendship.", response2);
        Files.deleteIfExists(Path.of("testCaseFileNameForWriting.txt"));
    }

    @Test
    void testCreateGroup() throws IOException {
        String[] command = new String[3];
        command[0] = "login";
        command[1] = "u1 p1";
        command[2] = "1";
        String response = serverCommandExecutor.execute(command);
        assertEquals("Successfully logged in as user u1.", response);
        String[] command2 = new String[3];
        command2[0] = "create-group";
        command2[1] = "groupWithU5 u5";
        command2[2] = "1";
        String response2 = serverCommandExecutor.execute(command2);
        assertEquals("Successfully created group.", response2);
        Files.deleteIfExists(Path.of("testCaseFileNameForWriting.txt"));
    }

    @Test
    void testSplit() throws IOException {
        String[] command = new String[3];
        command[0] = "login";
        command[1] = "u1 p1";
        command[2] = "1";
        String response = serverCommandExecutor.execute(command);
        assertEquals("Successfully logged in as user u1.", response);
        String[] command2 = new String[3];
        command2[0] = "split";
        command2[1] = "10 u2";
        command2[2] = "1";
        String response2 = serverCommandExecutor.execute(command2);
        assertEquals("Updated balance for the relationship of users u1 and u2.", response2);
        Files.deleteIfExists(Path.of("testCaseFileNameForWriting.txt"));
    }

    @Test
    void testSplitGroup() throws IOException {
        String[] command = new String[3];
        command[0] = "login";
        command[1] = "u1 p1";
        command[2] = "1";
        String response = serverCommandExecutor.execute(command);
        assertEquals("Successfully logged in as user u1.", response);
        String[] command2 = new String[3];
        command2[0] = "split-group";
        command2[1] = "10 gg mn ste gotini";
        command2[2] = "1";
        String response2 = serverCommandExecutor.execute(command2);
        assertEquals("Completed split between group.", response2);
        Files.deleteIfExists(Path.of("testCaseFileNameForWriting.txt"));
    }

    @Test
    void testReceivePayment() throws IOException {
        String[] command = new String[3];
        command[0] = "login";
        command[1] = "u1 p1";
        command[2] = "1";
        String response = serverCommandExecutor.execute(command);
        assertEquals("Successfully logged in as user u1.", response);
        String[] command2 = new String[3];
        command2[0] = "receive-payment";
        command2[1] = "10 u2";
        command2[2] = "1";
        String response2 = serverCommandExecutor.execute(command2);
        assertEquals("Successfully accepted payment.", response2);
        Files.deleteIfExists(Path.of("testCaseFileNameForWriting.txt"));
    }

    @Test
    void testGetStatus() throws IOException {
        String[] command = new String[3];
        command[0] = "login";
        command[1] = "u1 p1";
        command[2] = "1";
        String response = serverCommandExecutor.execute(command);
        assertEquals("Successfully logged in as user u1.", response);
        String[] command2 = new String[3];
        command2[0] = "get-status";
        command2[1] = "";
        command2[2] = "1";
        String response2 = serverCommandExecutor.execute(command2);
        assertEquals("u2 owes you 6.25lv.\n" +
                "u3 owes you 1.25lv.\n" +
                "u4 owes you 1.25lv.\n", response2);
        String[] command3 = new String[3];
        command3[0] = "split";
        command3[1] = "2 u2";
        command3[2] = "1";
        serverCommandExecutor.execute(command3);
        String[] command4 = new String[3];
        command4[0] = "get-status";
        command4[1] = "";
        command4[2] = "1";
        String response3 = serverCommandExecutor.execute(command4);
        assertEquals("u2 owes you 7.25lv.\n" +
                "u3 owes you 1.25lv.\n" +
                "u4 owes you 1.25lv.\n", response3);
        Files.deleteIfExists(Path.of("testCaseFileNameForWriting.txt"));
    }
}