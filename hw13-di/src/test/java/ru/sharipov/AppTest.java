package ru.sharipov;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.sharipov.appcontainer.AppComponentsContainerImpl;
import ru.sharipov.config.AppConfig;
import ru.sharipov.services.EquationPreparer;
import ru.sharipov.services.IOService;
import ru.sharipov.services.PlayerService;

import java.io.PrintStream;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class AppTest {
    @DisplayName("Из контекста тремя способами должен корректно доставаться компонент с проставленными полями")
    @ParameterizedTest(name = "Достаем по: {0}")
    @CsvSource(value = {"GameProcessor, ru.sharipov.services.GameProcessor",
            "GameProcessorImpl, ru.sharipov.services.GameProcessor",
            "gameProcessor, ru.sharipov.services.GameProcessor",

            "IOService, ru.sharipov.services.IOService",
            "IOServiceConsole, ru.sharipov.services.IOService",
            "ioService, ru.sharipov.services.IOService",

            "PlayerService, ru.sharipov.services.PlayerService",
            "PlayerServiceImpl, ru.sharipov.services.PlayerService",
            "playerService, ru.sharipov.services.PlayerService",

            "EquationPreparer, ru.sharipov.services.EquationPreparer",
            "EquationPreparerImpl, ru.sharipov.services.EquationPreparer",
            "equationPreparer, ru.sharipov.services.EquationPreparer"
    })
    public void shouldExtractFromContextCorrectComponentWithNotNullFields(String classNameOrBeanId, Class<?> rootClass) throws Exception {
        var ctx = new AppComponentsContainerImpl(AppConfig.class);

        assertThat(classNameOrBeanId).isNotEmpty();
        Object component;
        if (classNameOrBeanId.charAt(0) == classNameOrBeanId.toUpperCase().charAt(0)) {
            Class<?> gameProcessorClass = Class.forName("ru.sharipov.services." + classNameOrBeanId);
            assertThat(rootClass).isAssignableFrom(gameProcessorClass);

            component = ctx.getAppComponent(gameProcessorClass);
        } else {
            component = ctx.getAppComponent(classNameOrBeanId);
        }
        assertThat(component).isNotNull();
        assertThat(rootClass).isAssignableFrom(component.getClass());

        var fields = Arrays.stream(component.getClass().getDeclaredFields())
                .filter(f -> !Modifier.isStatic(f.getModifiers()))
                .peek(f -> f.setAccessible(true))
                .collect(Collectors.toList());

        for (var field: fields){
            var fieldValue = field.get(component);
            assertThat(fieldValue).isNotNull().isInstanceOfAny(IOService.class, PlayerService.class,
                    EquationPreparer.class, PrintStream.class, Scanner.class);
        }

    }
}