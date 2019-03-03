package de.kreth.kata.spieldeslebens.swing;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class OceanFieldTransferable implements Transferable {

	private static final DataFlavor[] FLAVOURS = { new DataFlavor(OceanFieldData.class, "OceanField") };

	private final OceanFieldData data;

	public OceanFieldTransferable(OceanFieldData data) {
		this.data = data;
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return FLAVOURS;
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return flavor.equals(FLAVOURS[0]);
	}

	@Override
	public OceanFieldData getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
		return data;
	}

}
