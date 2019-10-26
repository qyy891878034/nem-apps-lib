package io.nem.apps.main;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import io.nem.apps.api.TransactionApi;
import org.junit.Ignore;
import org.junit.Test;
import org.nem.core.connect.client.NisApiId;
import org.nem.core.model.TransferTransaction;
import org.nem.core.model.ncc.AccountMetaDataPair;
import org.nem.core.model.ncc.TransactionMetaDataPair;
import org.nem.core.model.ncc.UnconfirmedTransactionMetaDataPair;
import org.nem.core.serialization.Deserializer;
import io.nem.apps.api.AccountApi;
import io.nem.apps.service.NemAppsLibGlobals;

public class AccountApiTest extends NemAppsUnitTest {

	@Test
	public void testDeserializeAccount() {

		try {
			final CompletableFuture<Deserializer> des = NemAppsLibGlobals.CONNECTOR.getAsync(NemAppsLibGlobals.getNodeEndpoint(),
					NisApiId.NIS_REST_ACCOUNT_LOOK_UP, "address=" + TEST_DM_ADDRESS);

			des.thenAcceptAsync(d -> {
				assertNotNull(new AccountMetaDataPair(d).getEntity().getBalance());
			}).exceptionally(e -> {
				System.out.println(e.getMessage());
				return null;
			}).get();

		} catch (InterruptedException | ExecutionException e) {
			assert (false);
		}
	}

	@Test
	public void test() throws ExecutionException, InterruptedException {

	}

	@Test
	public void testDeserializeAccountPk() {
		try {
			final CompletableFuture<Deserializer> des = NemAppsLibGlobals.CONNECTOR.getAsync(NemAppsLibGlobals.getNodeEndpoint(),
					NisApiId.NIS_REST_ACCOUNT_UNCONFIRMED, "address=" + TEST_DM_ADDRESS);
			des.thenAcceptAsync(d -> {
				System.out.println(d.readObjectArray("data", UnconfirmedTransactionMetaDataPair::new).size());
				assertTrue(d.readObjectArray("data", UnconfirmedTransactionMetaDataPair::new).size() == 0);
			}).exceptionally(e -> {
				return null;
			}).get();
			assert (true);

		} catch (Exception e) {
			assert (false);
		}
	}

	@Test
	public void testAccountApiAddress() {
		try {
			assertNotNull(AccountApi.getAccountByAddress(TEST_DM_ADDRESS).getEntity());
		} catch (InterruptedException | ExecutionException e) {
			assert(false);
		}
	}

	
	@Test
	public void testAccountApiAllOwnedMosaic() {
		try {
			assertNotNull(AccountApi.getAccountOwnedMosaic(TEST_DM_ADDRESS));
		} catch (InterruptedException | ExecutionException e) {
			assert(false);
		}

	}
	
	@Test
	public void testAccountApiGetHarvestsInfo() {
		try {
			assertNotNull(AccountApi.getAccountHarvestInfo(TEST_DM_ADDRESS));
		} catch (InterruptedException | ExecutionException e) {
			assert(false);
		}

	}

	@Test
	public void testGenerteNewAccount() {
		assertNotNull(AccountApi.generateAccount().getAccount());
	}
}
