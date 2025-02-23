package expresslibrary.logic.commands;

import static expresslibrary.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static expresslibrary.logic.parser.CliSyntax.PREFIX_EMAIL;
import static expresslibrary.logic.parser.CliSyntax.PREFIX_NAME;
import static expresslibrary.logic.parser.CliSyntax.PREFIX_PHONE;
import static expresslibrary.logic.parser.CliSyntax.PREFIX_TAG;
import static java.util.Objects.requireNonNull;

import expresslibrary.logic.commands.exceptions.CommandException;
import expresslibrary.model.Model;
import expresslibrary.model.person.Person;

/**
 * Adds a person to the express library.
 */
public class AddPersonCommand extends Command {

    public static final String COMMAND_WORD = "addPerson";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a person to the express library. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL "
            + PREFIX_ADDRESS + "ADDRESS "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_EMAIL + "johnd@example.com "
            + PREFIX_ADDRESS + "311, Clementi Ave 2, #02-25 "
            + PREFIX_TAG + "friends "
            + PREFIX_TAG + "owesMoney ";

    public static final String MESSAGE_SUCCESS = "New person added: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the express library.";

    private final Person toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Person}
     */
    public AddPersonCommand(Person person) {
        requireNonNull(person);
        toAdd = person;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasPerson(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.addPerson(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddPersonCommand // instanceof handles nulls
                && toAdd.equals(((AddPersonCommand) other).toAdd));
    }
}
