package com.baseproject.interview.product

import com.baseproject.interview.feature.product.ProductContract
import com.baseproject.interview.feature.product.ProductInteractor
import com.baseproject.interview.feature.product.ProductPresenter
import com.baseproject.interview.mockProducts
import com.nhaarman.mockitokotlin2.capture
import com.nhaarman.mockitokotlin2.verify
import com.xwray.groupie.Section
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.MockitoAnnotations


class ProductPresenterTest {
    @Mock
    private lateinit var view: ProductContract.View
    @Mock
    private lateinit var interactor: ProductContract.Interactor
    @Captor
    private lateinit var getProductCallbackCaptor: ArgumentCaptor<ProductInteractor.GetProductCallback>
    private lateinit var presenter: ProductPresenter
    private val clickProductDetail: (String) -> Unit = {}

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter =
            ProductPresenter(interactor)
        presenter.takeView(view)
    }

    @Test
    fun `should return a list of features`() {
        presenter.loadData()
        presenter.mapProductItems(mockProducts(), clickProductDetail)

        verify(interactor).requestData(capture(getProductCallbackCaptor))
        getProductCallbackCaptor.value.onProductLoaded(mockProducts())

        verify(view).setUpGridList(1, mockProducts())
        verify(view).showProducts(Section())
    }

    @Test
    fun `should show a error message`() {
        presenter.loadData()
        verify(interactor).requestData(capture(getProductCallbackCaptor))
        getProductCallbackCaptor.value.onDataNotAvailable("data not available.")
        verify(view).showDataError()
    }
}