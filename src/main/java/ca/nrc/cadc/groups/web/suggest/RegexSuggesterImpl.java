/*
 ************************************************************************
 ****  C A N A D I A N   A S T R O N O M Y   D A T A   C E N T R E  *****
 *
 * (c) 2014.                         (c) 2014.
 * National Research Council            Conseil national de recherches
 * Ottawa, Canada, K1A 0R6              Ottawa, Canada, K1A 0R6
 * All rights reserved                  Tous droits reserves
 *
 * NRC disclaims any warranties         Le CNRC denie toute garantie
 * expressed, implied, or statu-        enoncee, implicite ou legale,
 * tory, of any kind with respect       de quelque nature que se soit,
 * to the software, including           concernant le logiciel, y com-
 * without limitation any war-          pris sans restriction toute
 * ranty of merchantability or          garantie de valeur marchande
 * fitness for a particular pur-        ou de pertinence pour un usage
 * pose.  NRC shall not be liable       particulier.  Le CNRC ne
 * in any event for any damages,        pourra en aucun cas etre tenu
 * whether direct or indirect,          responsable de tout dommage,
 * special or general, consequen-       direct ou indirect, particul-
 * tial or incidental, arising          ier ou general, accessoire ou
 * from the use of the software.        fortuit, resultant de l'utili-
 *                                      sation du logiciel.
 *
 *
 * @author jenkinsd
 * 10/16/14 - 1:29 PM
 *
 *
 *
 ****  C A N A D I A N   A S T R O N O M Y   D A T A   C E N T R E  *****
 ************************************************************************
 */

package ca.nrc.cadc.groups.web.suggest;

import java.util.*;


public class RegexSuggesterImpl<T> implements Suggester<T> {
    private final Matcher<String, T> matcher;
    private final int maxSuggestionCount;
    private final Comparator<T> resultsComparator;


    /**
     * Suggester based on Regex pattern matching.
     *
     * @param matcher            Regex matcher to use.
     * @param maxSuggestionCount Maximum number of suggestions to return.
     * @param resultsComparator  Comparator to present the results.
     */
    public RegexSuggesterImpl(final Matcher<String, T> matcher, final int maxSuggestionCount,
                              final Comparator<T> resultsComparator) {
        this.matcher = matcher;
        this.maxSuggestionCount = maxSuggestionCount;
        this.resultsComparator = resultsComparator;
    }


    /**
     * Obtain a list of T items from the given search term.
     *
     * @param term       The search term.
     * @param dataSource The in-memory data.
     * @return List of T instances, or empty List.  Never null.
     * List is used here to maintain order.
     */
    @Override
    public SuggestionResults<T> search(final String term, final Collection<T> dataSource) {
        final Set<T> sortedMatches;

        if (resultsComparator == null) {
            sortedMatches = new HashSet<>();
        } else {
            sortedMatches = new TreeSet<>(resultsComparator);
        }

        for (final T nextItem : dataSource) {
            if (matcher.matches(term, nextItem)) {
                sortedMatches.add(nextItem);
            }
        }

        final List<T> restrictedItems = new ArrayList<>();
        final int remainingResultsCount;

        if (maxSuggestionCount > 0) {
            for (final Iterator<T> sortedMatchesIterator = sortedMatches.iterator();
                 sortedMatchesIterator.hasNext() && (restrictedItems.size() < maxSuggestionCount); ) {
                restrictedItems.add(sortedMatchesIterator.next());
            }

            final int originalSize = sortedMatches.size();
            remainingResultsCount = (originalSize > maxSuggestionCount)
                ? (originalSize - maxSuggestionCount)
                : -1;
        } else {
            restrictedItems.addAll(sortedMatches);
            remainingResultsCount = -1;
        }

        return new SuggestionResults<>(restrictedItems, remainingResultsCount);
    }
}
