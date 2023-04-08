package expresslibrary.logic.parser;

import static expresslibrary.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static expresslibrary.logic.parser.CliSyntax.PREFIX_BOOK;
import static java.util.Objects.requireNonNull;

import java.util.stream.Stream;

import expresslibrary.commons.core.Messages;
import expresslibrary.commons.core.index.Index;
import expresslibrary.logic.commands.ReturnCommand;
import expresslibrary.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ReturnCommand object
 */
public class ReturnCommandParser implements Parser<ReturnCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ReturnCommand
     * and returns an ReturnCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public ReturnCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_BOOK);

        if (!arePrefixesPresent(argMultimap, PREFIX_BOOK)
                || argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ReturnCommand.MESSAGE_USAGE));
        }

        Index personIndex;
        try {
            personIndex = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, pe);
        }

        Index bookIndex;
        try {
            bookIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_BOOK).orElse(""));
        } catch (ParseException pe) {
            throw new ParseException(Messages.MESSAGE_INVALID_BOOK_DISPLAYED_INDEX, pe);
        }

        return new ReturnCommand(personIndex, bookIndex);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values
     * in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
