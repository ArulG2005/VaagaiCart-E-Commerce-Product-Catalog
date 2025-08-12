import {addCartItemRequest, addCartItemSuccess} from '../slices/cartSlice';
import axios from 'axios'

export const addCartItem = (id, quantity) => async(dispatch) => {
    try {
        dispatch(addCartItemRequest())
        const {data } = await axios.get(`http://localhost:8080/api/v1/product/${id}`)
        dispatch(addCartItemSuccess({
           productId: data.id,
            name: data.name,
            price: data.price,
           imageUrl: data.images.length > 0 ? data.images[0].imageUrl : '',
            stock: data.stock,
            quantity
        }))
    } catch (error) {
        console.log("error...",error);
        dispatch(addCartItemFailure(error.response.data.message));
    }
}