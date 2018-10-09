package fi.joniaromaa.lobbyplugin.utils;

public class StringUtils
{
	public static String join(String dec, String[] array, int start)
	{
		StringBuilder sb = new StringBuilder();
		for (int i = start; i < array.length; i++)
		{
			if (sb.length() > 0)
			{
				sb.append(dec);
			}
			
		    sb.append(array[i]);
		}
		
		return sb.toString();
	}
}
