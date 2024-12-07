package walletTests;


import com.example.walletAPI.WalletApiApplication;
import com.example.walletAPI.dto.WalletDTO;
import com.example.walletAPI.dto.WalletRequest;
import com.example.walletAPI.enam.OperationType;
import com.example.walletAPI.exception.modelException.IncorrectDataException;
import com.example.walletAPI.exception.modelException.NotFoundException;
import com.example.walletAPI.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(classes = WalletApiApplication.class)
@Transactional
public class WalletTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private WalletService walletService;

    private UUID walletId;

    @BeforeEach
    public void setUp() {
        // Создаем кошелек и устанавливаем начальный баланс
        walletId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
        jdbcTemplate.update("INSERT INTO wallet (wallet_id, amount) VALUES (?::uuid, ?)", walletId, "1000.00");
    }

    @Test
    public void testDeposit() {
        // Создаем объект WalletRequest для пополнения на 500
        WalletRequest walletRequest = new WalletRequest();
        walletRequest.setWalletId(walletId);
        walletRequest.setAmount(new BigDecimal("500.00"));
        walletRequest.setOperationType(OperationType.DEPOSIT);

        // Пополнение счета
        WalletDTO walletDTO = walletService.makeADeposit(walletRequest);

        // Проверяем, что баланс увеличился на 500
        BigDecimal amount = jdbcTemplate.queryForObject(
                "SELECT amount FROM wallet WHERE wallet_id = ?::uuid",
                new Object[]{walletId},
                BigDecimal.class
        );
        assertEquals(new BigDecimal("1500.00"), walletDTO.getAmount()); // Ожидаем 1500.00 после пополнения
    }

    @Test
    public void testWithdraw() {
        // Создаем объект WalletRequest для списания 500
        WalletRequest walletRequest = new WalletRequest();
        walletRequest.setWalletId(walletId);
        walletRequest.setAmount(new BigDecimal("500.00"));
        walletRequest.setOperationType(OperationType.WITHDRAW);

        // Списание со счета
        WalletDTO walletDTO = walletService.makeADeposit(walletRequest);

        // Проверяем, что баланс списан в размере 500
        BigDecimal amount = jdbcTemplate.queryForObject(
                "SELECT amount FROM wallet WHERE wallet_id = ?::uuid",
                new Object[]{walletId},
                BigDecimal.class
        );
        assertEquals(new BigDecimal("500.00"), walletDTO.getAmount()); // Ожидаем 500.00 после списания
    }

    @Test
    public void testWithdrawInsufficientFunds() {
        // Создаем объект WalletRequest для попытки снятия 2000
        WalletRequest walletRequest = new WalletRequest();
        walletRequest.setWalletId(walletId);
        walletRequest.setAmount(new BigDecimal("2000.00")); // Больше, чем доступно
        walletRequest.setOperationType(OperationType.WITHDRAW);

        // Ожидаем выброс исключения IncorrectDataException
        assertThrows(IncorrectDataException.class, () -> {
            walletService.makeADeposit(walletRequest); // Здесь вызываем метод снятия средств
        });
    }

    @Test
    public void testInvalidWalletId() {
        // Создаем недопустимый walletId
        UUID invalidWalletId = UUID.fromString("00000000-0000-0000-0000-000000000000");

        // Создаем объект WalletRequest для попытки снятия
        WalletRequest walletRequest = new WalletRequest();
        walletRequest.setWalletId(invalidWalletId);
        walletRequest.setAmount(new BigDecimal("100.00"));
        walletRequest.setOperationType(OperationType.WITHDRAW);

        // Ожидаем выброс исключения WalletNotFoundException
        assertThrows(NotFoundException.class, () -> {
            walletService.makeADeposit(walletRequest);
        });
    }

    @Test
    public void testGetData() {

        // Ожидаем, что метод вернет правильные данные о кошельке
        WalletDTO walletDTO = walletService.getData(walletId);

        // Проверяем, что данные о кошельке корректны
        assertNotNull(walletDTO);
        assertEquals(walletId, walletDTO.getWalletId());
        assertEquals(new BigDecimal("1000.00"), walletDTO.getAmount());
    }

    @Test
    public void testGetDataWalletNotFound() {
        // Создаем invalid walletId, который не существует в базе данных
        UUID invalidWalletId = UUID.fromString("00000000-0000-0000-0000-000000000000");

        // Ожидаем, что метод выбросит исключение WalletNotFoundException
        assertThrows(NotFoundException.class, () -> {
            walletService.getData(invalidWalletId);
        });
    }
}
