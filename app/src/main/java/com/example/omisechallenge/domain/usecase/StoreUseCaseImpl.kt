package com.example.omisechallenge.domain.usecase

import com.example.omisechallenge.data.repository.StoreRepository
import com.example.omisechallenge.domain.model.Product
import com.example.omisechallenge.domain.model.Store
import javax.inject.Inject

class StoreUseCaseImpl @Inject constructor(
    private val storeRepository: StoreRepository
): StoreUseCase {

    override suspend fun getStoreInfo(): Store {
        return Store("Mock Store", 4.7, "15:01:01.772Z", "19:45:51.365Z")
//        return storeRepository.getStoreInfo()
    }

    override suspend fun getProducts(): List<Product> {
        return listOf(
            Product("Mock Product 1", 30, "https://www.nespresso.com/ncp/res/uploads/recipes/nespresso-recipes-Latte-Art-Tulip.jpg"),
            Product("Mock Product 2", 40, "https://www.nespresso.com/shared_res/mos/free_html/sg/b2b/b2ccoffeerecipes/listing-image/image/dark-tiramisu-mocha.jpg"),
            Product("Mock Product 1", 30, "https://www.nespresso.com/ncp/res/uploads/recipes/nespresso-recipes-Latte-Art-Tulip.jpg"),
            Product("Mock Product 2", 40, "https://www.nespresso.com/shared_res/mos/free_html/sg/b2b/b2ccoffeerecipes/listing-image/image/dark-tiramisu-mocha.jpg"),
            Product("Mock Product 1", 30, "https://www.nespresso.com/ncp/res/uploads/recipes/nespresso-recipes-Latte-Art-Tulip.jpg"),
            Product("Mock Product 2", 40, "https://www.nespresso.com/shared_res/mos/free_html/sg/b2b/b2ccoffeerecipes/listing-image/image/dark-tiramisu-mocha.jpg"),
            Product("Mock Product 1", 30, "https://www.nespresso.com/ncp/res/uploads/recipes/nespresso-recipes-Latte-Art-Tulip.jpg"),
            Product("Mock Product 2", 40, "https://www.nespresso.com/shared_res/mos/free_html/sg/b2b/b2ccoffeerecipes/listing-image/image/dark-tiramisu-mocha.jpg"),
            Product("Mock Product 1", 30, "https://www.nespresso.com/ncp/res/uploads/recipes/nespresso-recipes-Latte-Art-Tulip.jpg"),
            Product("Mock Product 2", 40, "https://www.nespresso.com/shared_res/mos/free_html/sg/b2b/b2ccoffeerecipes/listing-image/image/dark-tiramisu-mocha.jpg"),
            Product("Mock Product 1", 30, "https://www.nespresso.com/ncp/res/uploads/recipes/nespresso-recipes-Latte-Art-Tulip.jpg"),
            Product("Mock Product 2", 40, "https://www.nespresso.com/shared_res/mos/free_html/sg/b2b/b2ccoffeerecipes/listing-image/image/dark-tiramisu-mocha.jpg")
        )

//        return storeRepository.getProducts()
    }
}