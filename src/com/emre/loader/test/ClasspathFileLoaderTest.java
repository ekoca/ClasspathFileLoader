package com.emre.loader.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.core.MultivaluedMap;

import com.emre.loader.ClasspathFileLoader;
import com.emre.loader.InputValidationException;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * Loads and caches files from the classpath.
 * 
 * @author Emre Koca
 */
public class ClasspathFileLoaderTest {
	private static final Pattern READ_ONLY_PATTERN = Pattern.compile(
			"\\s+for\\s+read\\s+only$", Pattern.MULTILINE
					| Pattern.CASE_INSENSITIVE);
	private static final Pattern PARAM_PATTERN = Pattern
			.compile("\\$\\{[_\\.a-zA-Z0-9]+\\}");

	public static void main(String[] args) {
		MultivaluedMap<String, String> parameterValues = new MultivaluedMapImpl();
		parameterValues.putSingle("id", "CQ12345");

		ClasspathFileLoaderTest testCase = new ClasspathFileLoaderTest();
		final String query = cleanSql(testCase.getInstance().get("getProfile.sql"));

		ParsedQuery q = new ParsedQuery();
		q.sql = cleanQuery(query);
		q.parameters = getParams(query);

		System.out.println(q.toString());
		
		try {
			System.out.println("################################################");
			String[] paramValues = getParamValues(parameterValues, q.parameters);
		} catch (InputValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ClasspathFileLoader getInstance(){
		System.out.println("Class path: " + getClass());
		System.out.println("################################################");
		return new ClasspathFileLoader(getClass());
	}

	// My data structure 
	private static class ParsedQuery {
		String sql;
		String[] parameters;

		@Override
		public String toString() {
			return "ParsedQuery by Emre [sql=" + sql + ", parameters="
					+ Arrays.toString(parameters) + "]";
		}
	}

	// Removes all the comment starting with //
	public static final String removeComments(String s) {
		return s.replaceAll("\\s*//.*", "");
	}

	// This will remove comment and trim the query. 
	// Also adds FOR READ ONLY if it is a select query
	protected static String cleanSql(String sql) {
		sql = removeComments(sql).trim();
		if (sql.regionMatches(true, 0, "select", 0, 6)
				&& (READ_ONLY_PATTERN.matcher(sql).find() == false))
			sql += "\nFOR READ ONLY";

		return sql;
	}

	// This will replace all the parameter starting with $ to ?
	public static String cleanQuery(String query) {
		return PARAM_PATTERN.matcher(query).replaceAll("?");
	}

	// This will get SQL query parameter names
	public static String[] getParams(String query) {
		ArrayList<String> params = new ArrayList<String>(2);
		Matcher m = PARAM_PATTERN.matcher(query);
		while (m.find()) {
			params.add(query.substring(m.start() + 2, m.end() - 1));
		}

		return params.toArray(new String[params.size()]);
	}

	// This will get the parameter values
	protected static String[] getParamValues(
			MultivaluedMap<String, String> values, String[] paramNames)
			throws InputValidationException {
		String[] paramValues = new String[paramNames.length];
		for (int i = 0; i < paramNames.length; i++) {
			final String value = values.getFirst(paramNames[i]);
			if (value == null)
				throw new InputValidationException("Missing input parameter '"
						+ paramNames[i] + '\'');
			paramValues[i] = value;
			System.out.println("Value of " + paramNames[i] + " : " + value);
		}

		return paramValues;
	}
}
