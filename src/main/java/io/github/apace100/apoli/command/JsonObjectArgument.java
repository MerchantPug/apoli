package io.github.apace100.apoli.command;

import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.Util;
import net.minecraft.network.chat.TranslatableComponent;

import java.io.IOException;
import java.lang.reflect.Field;

public abstract class JsonObjectArgument<T> implements ArgumentType<T> {
	public static final DynamicCommandExceptionType ERROR_INVALID_JSON = new DynamicCommandExceptionType((p_87121_) -> new TranslatableComponent("argument.component.invalid", p_87121_));
	private static final Field JSON_READER_POS = Util.make(() -> {
		try {
			new JsonReader(new java.io.StringReader(""));
			Field field = JsonReader.class.getDeclaredField("pos");
			field.setAccessible(true);
			return field;
		} catch (NoSuchFieldException nosuchfieldexception) {
			throw new IllegalStateException("Couldn't get field 'pos' for JsonReader", nosuchfieldexception);
		}
	});
	private static final Field JSON_READER_LINESTART = Util.make(() -> {
		try {
			new JsonReader(new java.io.StringReader(""));
			Field field = JsonReader.class.getDeclaredField("lineStart");
			field.setAccessible(true);
			return field;
		} catch (NoSuchFieldException nosuchfieldexception) {
			throw new IllegalStateException("Couldn't get field 'lineStart' for JsonReader", nosuchfieldexception);
		}
	});

	protected abstract T convert(JsonReader reader) throws IOException;

	@Override
	public T parse(StringReader reader) throws CommandSyntaxException {
		try {
			try {
				JsonReader jsonreader = new JsonReader(new java.io.StringReader(reader.getRemaining()));
				jsonreader.setLenient(false);
				T obj = this.convert(jsonreader);
				reader.setCursor(reader.getCursor() + getPos(jsonreader));
				if (obj == null)
					throw ERROR_INVALID_JSON.createWithContext(reader, "empty");
				else
					return obj;
			} catch (StackOverflowError | IOException ioexception) {
				throw new JsonParseException(ioexception);
			}
		} catch (Exception exception) {
			String s = exception.getCause() != null ? exception.getCause().getMessage() : exception.getMessage();
			throw ERROR_INVALID_JSON.createWithContext(reader, s);
		}
	}

	private static int getPos(JsonReader pReader) {
		try {
			return JSON_READER_POS.getInt(pReader) - JSON_READER_LINESTART.getInt(pReader) + 1;
		} catch (IllegalAccessException illegalaccessexception) {
			throw new IllegalStateException("Couldn't read position of JsonReader", illegalaccessexception);
		}
	}
}
