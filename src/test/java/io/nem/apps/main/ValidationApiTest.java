package io.nem.apps.main;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import io.nem.apps.api.MosaicApi;
import io.nem.apps.api.TransactionApi;
import io.nem.apps.builders.TransferTransactionBuilder;
import io.nem.apps.model.GeneratedAccount;
import org.apache.commons.lang.StringUtils;
import org.bouncycastle.crypto.tls.HeartbeatExtension;
import org.junit.Ignore;
import org.junit.Test;
import org.nem.core.connect.client.NisApiId;
import org.nem.core.crypto.KeyPair;
import org.nem.core.crypto.PrivateKey;
import org.nem.core.model.*;
import org.nem.core.model.mosaic.Mosaic;
import org.nem.core.model.mosaic.MosaicId;
import org.nem.core.model.namespace.NamespaceId;
import org.nem.core.model.ncc.*;
import org.nem.core.model.primitive.Amount;
import org.nem.core.model.primitive.Quantity;
import org.nem.core.node.NodeEndpoint;
import org.nem.core.serialization.Deserializer;
import org.nem.core.utils.HexEncoder;

import io.nem.apps.api.AccountApi;
import io.nem.apps.api.ValidationApi;
import io.nem.apps.service.NemAppsLibGlobals;
import io.nem.apps.util.HexStringUtils;
import net.sf.json.util.NewBeanInstanceStrategy;

public class ValidationApiTest extends NemAppsUnitTest {

	@Test
	public void testValidationFail() {
		assertFalse(ValidationApi.isAddressValid("123123"));
		assertFalse(ValidationApi.isAddressValid("123123",new NodeEndpoint("http","104.128.226.60",7890)));
		assertFalse(ValidationApi.isAddressValid("123123","http","104.128.226.60",7890));
	}

	@Test
	public void ttt() throws ExecutionException, InterruptedException {
		List<TransactionMetaDataPair> list = TransactionApi.getAllTransactionsWithPageSize("NCTEWSUGNWVRBISTBSL3ZSN2HZ4L6HVX5WLBBZLT", "200");
		int count = 0;
		for (TransactionMetaDataPair item : list) {
			Transaction transaction = item.getEntity();
			if (transaction instanceof TransferTransaction) {
				TransferTransaction transaction1 = (TransferTransaction) transaction;
				Iterator<Mosaic> it = transaction1.getAttachment().getMosaics().iterator();
				String sender = transaction1.getSigner().getAddress().toString();
				String receiver = transaction1.getRecipient().getAddress().toString();
				String txHash = item.getMetaData().getHash().toString();
				while (it.hasNext()) {
					Mosaic mosaic = it.next();
					if (StringUtils.equals(mosaic.getMosaicId().getName(), "xcu") && StringUtils.equals(mosaic.getMosaicId().getNamespaceId().toString(), "xarbon")) {
						System.out.println("sender=" + sender + ",receiver=" + receiver + ",quantity=" + mosaic.getQuantity().toString() + ",txHash=" + txHash);
						count++;
					}
				}

			}
		}
		System.out.println(count);

//		GeneratedAccount generatedAccount = AccountApi.generateAccount();
//		Address address = generatedAccount.getAccount().getAddress();
//		System.out.println(generatedAccount.getKeyPair().getPrivateKey().toString());
//		System.out.println(generatedAccount.getKeyPair().getPublicKey().toString());
//		System.out.println(address);
//
//		String senderPrivateKey = "193bf1166a35e98a8e735bad67e13e46b446e8b16ed32f4d5fbf51ea5452c3d5";
//		String receiverAddress = "NBJZOMNEKDEJ7FE2CZAXVQLLIYTBYRAU3J4IBRFH";
//		Long amount = 1L;

//		TransferTransactionAttachment transferTransactionAttachment = new TransferTransactionAttachment();
//		transferTransactionAttachment.addMosaic(new MosaicId(new NamespaceId("xarbon"), "xcu"), Quantity.fromValue(2000000));
//
//		KeyPair keyPair = new KeyPair(PrivateKey.fromHexString("008f88a3b9dbfa9f2adba3af86313dee42c466e08c0a2f58210b25a040a199fbc7"));
//
//		NemAnnounceResult result = TransferTransactionBuilder.sender(new Account(keyPair))
//				.recipient(new Account(Address.fromEncoded("NCB46TPCVSVZBUKVIGINOBROQWZDR2ZOQVFJB74Y"))).amount(Amount.fromNem(1))
//				.attachment(transferTransactionAttachment)
//				.buildAndSendTransaction();
//
//
//		System.out.println(result.getTransactionHash());
	}
	
	@Test
	public void testValidationSuccess() {
		assertTrue(ValidationApi.isAddressValid("TBPJCYR4XKGPGC3JHINJBJTW57ZLWTABDFLZSTMD"));
		assertTrue(ValidationApi.isAddressValid("TBPJCYR4XKGPGC3JHINJBJTW57ZLWTABDFLZSTMD",new NodeEndpoint("http","104.128.226.60",7890)));
		assertTrue(ValidationApi.isAddressValid("TBPJCYR4XKGPGC3JHINJBJTW57ZLWTABDFLZSTMD","http","104.128.226.60",7890));
	}
	
	@Test
	public void testLightValidation() {
		assertTrue(ValidationApi.isAddressPatternValid("TBPJCY-R4XKGP-GC3JHI-NJBJTW-57ZLWT-ABDFLZ-STMD"));
		assertTrue(ValidationApi.isAddressPatternValid("TBPJCYR4XKGPGC3JHINJBJTW57ZLWTABDFLZSTMD"));
	}
}
