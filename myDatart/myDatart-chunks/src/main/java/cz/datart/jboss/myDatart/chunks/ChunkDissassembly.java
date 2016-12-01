package cz.datart.jboss.myDatart.chunks;

import java.util.List;

public interface ChunkDissassembly {

	public List<String> dissassemble(String xmlMessage, String entityName) throws Throwable;
}
