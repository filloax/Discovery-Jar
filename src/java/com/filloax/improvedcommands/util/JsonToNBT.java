//Credits to Tschallacka for translation http://www.minecraftforge.net/forum/index.php?topic=29852.0

package com.filloax.improvedcommands.util;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import cpw.mods.fml.common.registry.GameRegistry;

import java.util.Iterator;
import java.util.Stack;
import java.util.regex.Pattern;

import net.minecraft.item.Item;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JsonToNBT
{
    private static final Logger logger = LogManager.getLogger();
    private static final Pattern isIntegerOrStringArray = Pattern.compile("\\[[-+\\d|,\\s]+\\]");
//    private static final String __OBFID = "CL_00001232";
   /**
     * Get a compound tag from json string
     * @param input JSON String input
     * @return NBTTagCompound with the correct values
     * @throws NBTException
     */
    public static NBTTagCompound getCompoundTagFromString(String input) throws NBTException
    {
        input = input.trim();
        // if the item starts with a { it's a compound object
        if (!input.startsWith("{"))
        {
            throw new NBTException("Invalid tag encountered, expected \'{\' as first char.");
        }
        else if (checkNumberOfTopTags(input) != 1)
        {
            throw new NBTException("Encountered multiple top tags, only one expected");
        }
        else
        {
            return (NBTTagCompound)toJSONCompound("tag", input).getNBTValue();
        }
    }

    static int checkNumberOfTopTags(String checkThis) throws NBTException
    {
        int encountered = 0;
        boolean flag = false;
        Stack stack = new Stack();

        for (int i = 0; i < checkThis.length(); ++i)
        {
            char c0 = checkThis.charAt(i);
            //char 34 = "
            if (c0 == 34)
            {
                if (isValidJSONString(checkThis, i))
                {
                    if (!flag)
                    {
                        throw new NBTException("Illegal use of \\\": " + checkThis);
                    }
                }
                else
                {
                    flag = !flag;
                }
            }
            else if (!flag)
            {
            	// 123 = {
            	// 91 = [
                if (c0 != 123 && c0 != 91)
                {
                	// 123 = {
                	// 125 = }
                    if (c0 == 125 && (stack.isEmpty() || ((Character)stack.pop()).charValue() != 123))
                    {
                        throw new NBTException("Unbalanced curly brackets {}: " + checkThis);
                    }
                    // 91 = [
                    // 93 = [
                    if (c0 == 93 && (stack.isEmpty() || ((Character)stack.pop()).charValue() != 91))
                    {
                        throw new NBTException("Unbalanced square brackets []: " + checkThis);
                    }
                }
                else
                {
                    if (stack.isEmpty())
                    {
                        ++encountered;
                    }

                    stack.push(Character.valueOf(c0));
                }
            }
        }

        if (flag)
        {
            throw new NBTException("Unbalanced quotation: " + checkThis);
        }
        else if (!stack.isEmpty())
        {
            throw new NBTException("Unbalanced brackets: " + checkThis);
        }
        else
        {
            if (encountered == 0 && !checkThis.isEmpty())
            {
                encountered = 1;
            }

            return encountered;
        }
    }
    /**
     * Takes any number of elements array but will only use the first two elements of the array.
     * Will throw an ArrayIndexIsOutOfBounds exception when an array < 2 elements is supplied
     * @param arrayInputString Element at first index is name of object. Element at second index is contents
     * @return JSONtoNBT.Any
     * @throws NBTException, ArrayIndexOutOfBoundsException
     */
    static JsonToNBT.Any toJSONCompound(String ... arrayInputString) throws NBTException
    {
        return toJSONCompound(arrayInputString[0], arrayInputString[1]);
    }

    static JsonToNBT.Any toJSONCompound(String name, String contents) throws NBTException
    {
        contents = contents.trim();
        String subContents;
        boolean flag;
        char character;

        if (contents.startsWith("{"))
        {
            contents = contents.substring(1, contents.length() - 1);
            JsonToNBT.Compound compound;

            for (compound = new JsonToNBT.Compound(name); contents.length() > 0; contents = contents.substring(subContents.length() + 1))
            {
                subContents = getValueFromJSONTag(contents, true);

                if (subContents.length() > 0)
                {
                    flag = false;
                    compound.arrayList.add(getJSONCompound(subContents, flag));
                }

                if (contents.length() < subContents.length() + 1)
                {
                    break;
                }

                character = contents.charAt(subContents.length());
                // 44 = "
                // 123 = {
                // 125 = }
                // 91 = [
                // 93 = ]
                if (character != 44 && character != 123 && character != 125 && character != 91 && character != 93)
                {
                    throw new NBTException("Unexpected token \'" + character + "\' at: " + contents.substring(subContents.length()));
                }
            }

            return compound;
        }
        else if (contents.startsWith("[") && !isIntegerOrStringArray.matcher(contents).matches())
        {
            contents = contents.substring(1, contents.length() - 1);
            JsonToNBT.List list;

            for (list = new JsonToNBT.List(name); contents.length() > 0; contents = contents.substring(subContents.length() + 1))
            {
                subContents = getValueFromJSONTag(contents, false);

                if (subContents.length() > 0)
                {
                    flag = true;
                    list.arrayList.add(getJSONCompound(subContents, flag));
                }

                if (contents.length() < subContents.length() + 1)
                {
                    break;
                }

                character = contents.charAt(subContents.length());

                if (character != 44 && character != 123 && character != 125 && character != 91 && character != 93)
                {
                    throw new NBTException("Unexpected token \'" + character + "\' at: " + contents.substring(subContents.length()));
                }
            }

            return list;
        }
        else
        {
            return new JsonToNBT.Primitive(name, contents);
        }
    }
    
    //gets the item from the string id
    public static Item getItem(String s)
    {

    String modId = "minecraft";

    String name;

    if (s.indexOf(":") != -1)

            {

            modId = s.substring(0, s.indexOf(":"));

            }

    name = s.substring(s.indexOf(":") + 1);

    return GameRegistry.findItem(modId, name); // Will return null if no item is found.

    }
    
    /**
     * Takes the string and returns the Compound key => value pair
     * @param parseThis The string to retrieve the values from
     * @param errorMode Should we throw exceptions or not
     * @return JSONtoNBTAny key => value compound
     * @throws NBTException
     */
    
     //Edited to check if the key is "id"
    private static JsonToNBT.Any getJSONCompound(String parseThis, boolean errorMode) throws NBTException
    {
        String s1 = getKeyFromString(parseThis, errorMode);
        String s2 = getValueFromString(parseThis, errorMode);
//        if (s1 == "id") {
//        	System.out.println("If passed");
        	Item item = getItem(s2);
        	if (item != null) {
//            	System.out.println("Item:"+item.getUnlocalizedName());
        		s2 = String.valueOf(item.getIdFromItem(item));
//        		System.out.println("s2 at item if:"+s2);
        	}
//        }
//        System.out.println("\'"+s1+"\':\'"+s2+"\'");
        return toJSONCompound(new String[] {s1, s2});
    }
    /**
     * Gets the value from a JSON string
     * @param searchThis The string to search for first colon
     * @param errorMode Will throw exceptions if true, none if set to false
     * @return String Value
     * @throws NBTException on invalid JSON && errorModeSilent is set to true
     */
    private static String getValueFromJSONTag(String searchThis, boolean errorMode) throws NBTException
    {
        int locationOfColon = findCharacter(searchThis, ':');
        int locationOfComma = findCharacter(searchThis, ',');

        if (errorMode)
        {
            if (locationOfColon == -1)
            {
                throw new NBTException("Unable to locate name/value separator for string: " + searchThis);
            }

            if (locationOfComma != -1 && locationOfComma < locationOfColon)
            {
                throw new NBTException("Name error at: " + searchThis);
            }
        }
        else if (locationOfColon == -1 || locationOfColon > locationOfComma)
        {
            locationOfColon = -1;
        }

        return getValueToKey(searchThis, locationOfColon);
    }

    private static String getValueToKey(String parseThis, int startPos) throws NBTException
    {
        Stack stack = new Stack();
        int j = startPos + 1;
        boolean flag = false;
        boolean flag1 = false;
        boolean flag2 = false;

        for (int k = 0; j < parseThis.length(); ++j)
        {
            char character = parseThis.charAt(j);
            // 34 = "
            if (character == 34)
            {
                if (isValidJSONString(parseThis, j))
                {
                    if (!flag)
                    {
                        throw new NBTException("Illegal use of \\\": " + parseThis);
                    }
                }
                else
                {
                    flag = !flag;

                    if (flag && !flag2)
                    {
                        flag1 = true;
                    }

                    if (!flag)
                    {
                        k = j;
                    }
                }
            }
            else if (!flag)
            {
                if (character != 123 && character != 91)
                {
                	// 123 = {
                	// 125 = }
                    if (character == 125 && (stack.isEmpty() || ((Character)stack.pop()).charValue() != 123))
                    {
                        throw new NBTException("Unbalanced curly brackets {}: " + parseThis);
                    }
                    // 91 = [
                    // 93 = ]
                    if (character == 93 && (stack.isEmpty() || ((Character)stack.pop()).charValue() != 91))
                    {
                        throw new NBTException("Unbalanced square brackets []: " + parseThis);
                    }
                    // 44 = ,
                    if (character == 44 && stack.isEmpty())
                    {
                        return parseThis.substring(0, j);
                    }
                }
                else
                {
                    stack.push(Character.valueOf(character));
                }
            }

            if (!Character.isWhitespace(character))
            {
                if (!flag && flag1 && k != j)
                {
                    return parseThis.substring(0, k + 1);
                }

                flag2 = true;
            }
        }

        return parseThis.substring(0, j);
    }
    /**
     * Returns "" if it doesn't contain a key or contains a list/compound
     * @param parseThis The string to find the key from
     * @param errorMode Throws Exception if false when no key is found.
     * @return String key if found or string empty if not found
     * @throws NBTException
     */
    private static String getKeyFromString(String parseThis, boolean errorMode) throws NBTException
    {
        if (errorMode)
        {
            parseThis = parseThis.trim();

            if (parseThis.startsWith("{") || parseThis.startsWith("["))
            {
                return "";
            }
        }

        int i = findCharacter(parseThis, ':');

        if (i == -1)
        {
            if (errorMode)
            {
                return "";
            }
            else
            {
                throw new NBTException("Unable to locate name/value separator for string: " + parseThis);
            }
        }
        else
        {
            return parseThis.substring(0, i).trim();
        }
    }
    /**
     * Gets the value of the string.
     * @param parseThis The string to retrieve the value from
     * @param errorMode if set to false it will throw an error if it can't find a valid value location
     * @return String value str
     * @throws NBTException
     */
    private static String getValueFromString(String parseThis, boolean errorMode) throws NBTException
    {
        if (errorMode)
        {
            parseThis = parseThis.trim();

            if (parseThis.startsWith("{") || parseThis.startsWith("["))
            {
                return parseThis;
            }
        }

        int i = findCharacter(parseThis, ':');

        if (i == -1)
        {
            if (errorMode)
            {
                return parseThis;
            }
            else
            {
                throw new NBTException("Unable to locate name/value separator for string: " + parseThis);
            }
        }
        else
        {
            return parseThis.substring(i + 1).trim();
        }
    }

    private static int findCharacter(String input, char characterToFind)
    {
        int i = 0;

        for (boolean flag = true; i < input.length(); ++i)
        {
            char character = input.charAt(i);
            // 34 = "
            if (character == 34)
            {
                if (!isValidJSONString(input, i))
                {
                    flag = !flag;// good heavens... why not a break?
                }
            }
            else if (flag)
            {
                if (character == characterToFind)
                {
                    return i;
                }

                if (character == 123 || character == 91)
                {
                    return -1;
                }
            }
        }

        return -1;
    }

    private static boolean isValidJSONString(String parseThis, int locationToStart)
    {																		//92 = \
        return locationToStart > 0 && parseThis.charAt(locationToStart - 1) == 92 && !isValidJSONString(parseThis, locationToStart - 1);
    }

    abstract static class Any
        {
            protected String key;
            private static final String __OBFID = "CL_00001233";

            public abstract NBTBase getNBTValue();
        }

    static class Compound extends JsonToNBT.Any
        {
            protected java.util.List arrayList = Lists.newArrayList();
            private static final String __OBFID = "CL_00001234";

            public Compound(String p_i45137_1_)
            {
                this.key = p_i45137_1_;
            }

            public NBTBase getNBTValue()
            {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                Iterator iterator = this.arrayList.iterator();

                while (iterator.hasNext())
                {
                    JsonToNBT.Any any = (JsonToNBT.Any)iterator.next();
                    nbttagcompound.setTag(any.key, any.getNBTValue());
                }

                return nbttagcompound;
            }
        }

    static class List extends JsonToNBT.Any
        {
            protected java.util.List arrayList = Lists.newArrayList();
            private static final String __OBFID = "CL_00001235";

            public List(String keyValue)
            {
                this.key = keyValue;
            }

            public NBTBase getNBTValue()
            {
                NBTTagList nbttaglist = new NBTTagList();
                Iterator iterator = this.arrayList.iterator();

                while (iterator.hasNext())
                {
                    JsonToNBT.Any any = (JsonToNBT.Any)iterator.next();
                    nbttaglist.appendTag(any.getNBTValue());
                }

                return nbttaglist;
            }
        }

    static class Primitive extends JsonToNBT.Any
        {
            private static final Pattern DOUBLE = Pattern.compile("[-+]?[0-9]*\\.?[0-9]+[d|D]");
            private static final Pattern FLOAT = Pattern.compile("[-+]?[0-9]*\\.?[0-9]+[f|F]");
            private static final Pattern BYTE = Pattern.compile("[-+]?[0-9]+[b|B]");
            private static final Pattern LONG = Pattern.compile("[-+]?[0-9]+[l|L]");
            private static final Pattern STRING = Pattern.compile("[-+]?[0-9]+[s|S]");
            private static final Pattern INTEGER = Pattern.compile("[-+]?[0-9]+");
            private static final Pattern SECRETDOUBLE = Pattern.compile("[-+]?[0-9]*\\.?[0-9]+");
            private static final Splitter COMMASPLITTER = Splitter.on(',').omitEmptyStrings();
            protected String value;
            private static final String __OBFID = "CL_00001236";

            public Primitive(String key, String value)
            {
                this.key = key;
                this.value = value;
            }

            public NBTBase getNBTValue()
            {
                try
                {
                    if (DOUBLE.matcher(this.value).matches())
                    {
                        return new NBTTagDouble(Double.parseDouble(this.value.substring(0, this.value.length() - 1)));
                    }

                    if (FLOAT.matcher(this.value).matches())
                    {
                        return new NBTTagFloat(Float.parseFloat(this.value.substring(0, this.value.length() - 1)));
                    }

                    if (BYTE.matcher(this.value).matches())
                    {
                        return new NBTTagByte(Byte.parseByte(this.value.substring(0, this.value.length() - 1)));
                    }

                    if (LONG.matcher(this.value).matches())
                    {
                        return new NBTTagLong(Long.parseLong(this.value.substring(0, this.value.length() - 1)));
                    }

                    if (STRING.matcher(this.value).matches())
                    {
                        return new NBTTagShort(Short.parseShort(this.value.substring(0, this.value.length() - 1)));
                    }

                    if (INTEGER.matcher(this.value).matches())
                    {
                        return new NBTTagInt(Integer.parseInt(this.value));
                    }

                    if (SECRETDOUBLE.matcher(this.value).matches())
                    {
                        return new NBTTagDouble(Double.parseDouble(this.value));
                    }

                    if (this.value.equalsIgnoreCase("true") || this.value.equalsIgnoreCase("false"))
                    {
                        return new NBTTagByte((byte)(Boolean.parseBoolean(this.value) ? 1 : 0));
                    }
                }
                catch (NumberFormatException numberformatexception1)
                {
                    this.value = this.value.replaceAll("\\\\\"", "\"");
                    return new NBTTagString(this.value);
                }

                if (this.value.startsWith("[") && this.value.endsWith("]"))
                {
                    String s = this.value.substring(1, this.value.length() - 1);
                    String[] astring = (String[])Iterables.toArray(COMMASPLITTER.split(s), String.class);

                    try
                    {
                        int[] aint = new int[astring.length];

                        for (int j = 0; j < astring.length; ++j)
                        {
                            aint[j] = Integer.parseInt(astring[j].trim());
                        }

                        return new NBTTagIntArray(aint);
                    }
                    catch (NumberFormatException numberformatexception)
                    {
                        return new NBTTagString(this.value);
                    }
                }
                else
                {
                    if (this.value.startsWith("\"") && this.value.endsWith("\""))
                    {
                        this.value = this.value.substring(1, this.value.length() - 1);
                    }

                    this.value = this.value.replaceAll("\\\\\"", "\"");
                    StringBuilder stringbuilder = new StringBuilder();

                    for (int i = 0; i < this.value.length(); ++i)
                    {
                        if (i < this.value.length() - 1 && this.value.charAt(i) == 92 && this.value.charAt(i + 1) == 92)
                        {
                            stringbuilder.append('\\');
                            ++i;
                        }
                        else
                        {
                            stringbuilder.append(this.value.charAt(i));
                        }
                    }

                    return new NBTTagString(stringbuilder.toString());
                }
            }
        }
}