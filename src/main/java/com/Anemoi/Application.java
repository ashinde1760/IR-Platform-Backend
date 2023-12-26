package com.Anemoi;

import java.util.List;

import com.Anemoi.InvestorRelation.Configuration.ReadPropertiesFile;
import com.Anemoi.InvestorRelation.Configuration.InvestorDatabase;

import io.micronaut.runtime.Micronaut;

public class Application {
	public static void main(String[] args) {
		Micronaut.run(Application.class, args);
		try {
			List<String> tenantList = ReadPropertiesFile.getAllTenant();
			for (String tenant : tenantList) {

				String dataBaseName = ReadPropertiesFile.dataBaseName(tenant);
				System.out.println(dataBaseName);
				InvestorDatabase.createDataBases(dataBaseName);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
