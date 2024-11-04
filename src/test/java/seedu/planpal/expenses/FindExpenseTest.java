package seedu.planpal.expenses;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.planpal.exceptions.EmptyDescriptionException;
import seedu.planpal.exceptions.PlanPalExceptions;
import seedu.planpal.modes.expenses.ExpenseManager;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class FindExpenseTest {
    private static final ByteArrayOutputStream OUTPUT_STREAM = new ByteArrayOutputStream();
    private ExpenseManager expenseManager;

    @BeforeEach
    public void setUp() {
        expenseManager = new ExpenseManager();
        System.setOut(new PrintStream(OUTPUT_STREAM));
        OUTPUT_STREAM.reset();
    }

    @Test
    public void validDescription_success() {
        try {
            expenseManager.getBudgetManager().setBudget("1000");
            expenseManager.addExpense("/name:trial1 /cost:100");
            expenseManager.findExpense("trial1");
            assertTrue(OUTPUT_STREAM.toString().contains("[Name = trial1, Cost = $100]"));
        } catch (PlanPalExceptions e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void noMatch_exceptionThrown() {
        try {
            expenseManager.getBudgetManager().setBudget("1000");
            expenseManager.addExpense("/name:trial1 /cost:100");
            assertThrows(PlanPalExceptions.class, () -> expenseManager.findExpense("trial3"));
        } catch (PlanPalExceptions e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void emptyDescription_exceptionThrown() {
        assertThrows(EmptyDescriptionException.class, () -> expenseManager.findExpense(""));
    }

    @Test
    public void multipleMatches_success() {
        try {
            expenseManager.getBudgetManager().setBudget("1000");
            expenseManager.addExpense("/name:trial1 /cost:100");
            expenseManager.addExpense("/name:trial1 duplicate /cost:150");
            expenseManager.findExpense("trial1");
            assertTrue(OUTPUT_STREAM.toString().contains("[Name = trial1, Cost = $100]"));
            assertTrue(OUTPUT_STREAM.toString().contains("[Name = trial1 duplicate, Cost = $150]"));
        } catch (PlanPalExceptions e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void caseInsensitive_success() {
        try {
            expenseManager.getBudgetManager().setBudget("1000");
            expenseManager.addExpense("/name:TrialCase /cost:120");
            expenseManager.findExpense("trialcase");
            assertTrue(OUTPUT_STREAM.toString().contains("[Name = TrialCase, Cost = $120]"));
        } catch (PlanPalExceptions e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void multipleKeywords_success() {
        try {
            expenseManager.getBudgetManager().setBudget("1000");
            expenseManager.addExpense("/name:trial1 /cost:100");
            expenseManager.addExpense("/name:trial2 /cost:200");
            expenseManager.findExpense("trial1 trial2");
            assertTrue(OUTPUT_STREAM.toString().contains("[Name = trial1, Cost = $100]"));
            assertTrue(OUTPUT_STREAM.toString().contains("[Name = trial2, Cost = $200]"));
        } catch (PlanPalExceptions e) {
            fail(e.getMessage());
        }
    }
}
