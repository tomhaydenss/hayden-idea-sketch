package hayden.ideasketch.rest.concurrency.util;

import java.nio.ByteBuffer;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jboss.resteasy.util.Hex;

public class ETagHelper {
	
	public String gerar(Object entity) {
		int hashCode = HashCodeBuilder.reflectionHashCode(entity);
		byte[] array = ByteBuffer.allocate(4).putInt(hashCode).array();
		String eTag = Hex.encodeHex(array);
		return eTag;
	}

	public boolean validar(String eTag, Object entity) {
		return StringUtils.isNotEmpty(eTag) && gerar(entity).equals(eTag);
	}
	
}
