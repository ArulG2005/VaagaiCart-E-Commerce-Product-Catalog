import axios from 'axios';
import { productsFail, productsSuccess, productsRequest, adminProductsRequest, adminProductsSuccess, adminProductsFail } from '../slices/productsSlice';
import { productFail, productSuccess, productRequest, createReviewRequest, createReviewSuccess, createReviewFail, newProductRequest, newProductSuccess, newProductFail, deleteProductRequest, deleteProductSuccess, deleteProductFail, updateProductRequest, updateProductSuccess, updateProductFail, reviewsRequest, reviewsSuccess, reviewsFail, deleteReviewRequest, deleteReviewSuccess, deleteReviewFail } from '../slices/productSlice';

export const getProducts = (keyword, price, category, rating, currentPage) => async (dispatch) => {
  try {
    dispatch(productsRequest());
    let link = `http://localhost:8080/api/v1/products?page=${currentPage - 1}&size=3`;

    if (keyword) {
      link += `&keyword=${encodeURIComponent(keyword)}`;
    }
    if (price) {
      link += `&priceGte=${price[0]}&priceLte=${price[1]}`;
    }
    if (category) {
      link += `&category=${encodeURIComponent(category)}`;
    }
    if (rating) {
      link += `&ratings=${rating}`;
    }

    const { data } = await axios.get(link);
    dispatch(productsSuccess(data));
  } catch (error) {
    dispatch(productsFail(error.response?.data?.message || error.message));
  }
};



export const getProduct = id => async (dispatch) => {

    try {  
        dispatch(productRequest()) 
        const { data }  =  await axios.get(`http://localhost:8080/api/v1/product/${id}`);
        dispatch(productSuccess(data))
    } catch (error) {
        //handle error
        dispatch(productFail(error.response.data.message))
    }
    
}

export const createReview = reviewData => async (dispatch) => {

    try {  
        dispatch(createReviewRequest()) 
        const config = {
            headers : {
                'Content-type': 'application/json'
            }
        }
        const { data }  =  await axios.put(`http://localhost:8080/api/v1/review`,reviewData, config);
        dispatch(createReviewSuccess(data))
    } catch (error) {
        //handle error
        dispatch(createReviewFail(error.response.data.message))
    }
    
}

export const getAdminProducts  =  async (dispatch) => {

    try {  
        dispatch(adminProductsRequest()) 
        const { data }  =  await axios.get(`http://localhost:8080/api/v1/admin/products`);
        dispatch(adminProductsSuccess(data))
    } catch (error) {
        //handle error
    
    }
    
}

export const createNewProduct  =  productData => async (dispatch) => {

    try {  
        dispatch(newProductRequest()) 
        const { data }  =  await axios.post(`http://localhost:8080/api/v1/admin/product/new`, productData);
        dispatch(newProductSuccess(data))
    } catch (error) {
        //handle error
        dispatch(newProductFail(error.response.data.message))
    }
    
}

export const deleteProduct  =  id => async (dispatch) => {

    try {  
        dispatch(deleteProductRequest()) 
        await axios.delete(`http://localhost:8080/api/v1/admin/product/${id}`);
        dispatch(deleteProductSuccess())
    } catch (error) {
        //handle error
        dispatch(deleteProductFail(error.response.data.message))
    }
    
}

export const updateProduct  =  (id, productData) => async (dispatch) => {

    try {  
        dispatch(updateProductRequest()) 
        const { data }  =  await axios.put(`http://localhost:8080/api/v1/admin/product/${id}`, productData);
        dispatch(updateProductSuccess(data))
    } catch (error) {
        //handle error
        dispatch(updateProductFail(error.response.data.message))
    }
    
}


export const getReviews =  productId => async (dispatch) => {

    try {  
        dispatch(reviewsRequest()) 
        const { data }  =  await axios.get(`http://localhost:8080/api/v1/admin/reviews`,{params: {productId}});
        dispatch(reviewsSuccess(data))
    } catch (error) {
        //handle error
        dispatch(reviewsFail(error.response.data.message))
    }
    
}

export const deleteReview =  (productId, id) => async (dispatch) => {

    try {  
        dispatch(deleteReviewRequest()) 
        await axios.delete(`http://localhost:8080/api/v1/admin/review`,{params: {productId, id}});
        dispatch(deleteReviewSuccess())
    } catch (error) {
        //handle error
        dispatch(deleteReviewFail(error.response.data.message))
    }
    
}