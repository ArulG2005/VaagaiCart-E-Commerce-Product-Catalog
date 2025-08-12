import { useElements, useStripe } from "@stripe/react-stripe-js"
import { CardNumberElement, CardExpiryElement, CardCvcElement } from "@stripe/react-stripe-js";
import axios from "axios";
import { useEffect } from "react";
import {useDispatch, useSelector} from 'react-redux';
import {useNavigate} from 'react-router-dom'
import { toast } from "react-toastify";
import { orderCompleted } from "../../slices/cartSlice";
import {validateShipping} from '../cart/Shipping';
import {createOrder} from '../../actions/orderActions'
import { clearError as clearOrderError } from "../../slices/orderSlice";

export default function Payment() {
    const stripe = useStripe();
    const elements = useElements();
    const dispatch = useDispatch()
    const navigate = useNavigate();
    const orderInfo = JSON.parse(sessionStorage.getItem('orderInfo'))
    const { user } = useSelector(state => state.authState)
    const {items:cartItems, shippingInfo } = useSelector(state => state.cartState)
    const { error:orderError } = useSelector(state => state.orderState)

    const paymentData = {
        amount : Math.round( orderInfo.totalPrice * 100),
        shipping :{
            name: user.name,
            address:{
                city: shippingInfo.city,
                postal_code : shippingInfo.postalCode,
                country: shippingInfo.country,
                state: shippingInfo.state,
                line1 : shippingInfo.address
            },
            phone: shippingInfo.phoneNo
        }
    }
console.log("carrrr",cartItems)
   
    const order = {
        orderItems: cartItems,
        shippingInfo
    }

    if(orderInfo) {
        order.itemsPrice = orderInfo.itemsPrice
        order.shippingPrice = orderInfo.shippingPrice
        order.taxPrice = orderInfo.taxPrice
        order.totalPrice = orderInfo.totalPrice
        
    }

    useEffect(() => {
        validateShipping(shippingInfo, navigate)
        if(orderError) {
            toast(orderError, {
                position: "bottom-right",
                type: 'error',
                onOpen: ()=> { dispatch(clearOrderError()) }
            })
            return
        }

    },[])

const submitHandler = async (e) => {

console.log("productInfo:",paymentData)

    e.preventDefault();
    console.log('Submit handler triggered');
    
    const payBtn = document.querySelector('#pay_btn');
    if (payBtn) {
        payBtn.disabled = true;
        console.log('#pay_btn disabled');
    } else {
        console.warn('#pay_btn element not found');
    }

    try {
        console.log('Sending paymentData to backend:', paymentData);
        const { data } = await axios.post('http://localhost:8080/api/v1/payment/process', paymentData);
        console.log('Payment API response:', data);
        
        const clientSecret = data.client_secret;
        console.log('Received clientSecret:', clientSecret);

        const cardElement = elements.getElement(CardNumberElement);
        if (!cardElement) {
            console.error('CardNumberElement not found');
            if (payBtn) payBtn.disabled = false;
            return;
        } else {
            console.log('CardNumberElement found:', cardElement);
        }

        console.log('Calling stripe.confirmCardPayment with clientSecret and payment method details...');
        const result = await stripe.confirmCardPayment(clientSecret, {
            payment_method: {
                card: cardElement,
                billing_details: {
                    name: user.name,
                    email: user.email
                }
            }
        });

        console.log('Stripe confirmCardPayment result:', result);

        if (result.error) {
            console.error('Stripe error:', result.error.message);
            toast(result.error.message, {
                type: 'error',
                position: "bottom-right",
            });
            if (payBtn) payBtn.disabled = false;
        } else {
            console.log('PaymentIntent status:', result.paymentIntent.status);
            if (result.paymentIntent.status === 'succeeded') {
                toast('Payment Success!', {
                    type: 'success',
                     position: "bottom-right",
                });

                order.paymentInfo = {
                    id: result.paymentIntent.id,
                    status: result.paymentIntent.status
                };
                console.log('Order paymentInfo updated:', order.paymentInfo);

                dispatch(orderCompleted());
                console.log('Dispatched orderCompleted');
                console.log('Creating order with:..................................', order);
                dispatch(createOrder(order));
                console.log('Dispatched createOrder');

                navigate('/order/success');
                console.log('Navigation to /order/success');
            } else {
                toast('Please Try again!', {
                    type: 'warning',
                    position: "bottom-center"
                });
                if (payBtn) payBtn.disabled = false;
            }
        }
    } catch (error) {
        console.error('Error in submitHandler:', error);
        toast('Payment failed. Please try again later.', {
            type: 'error',
             position: "bottom-right",
        });
        const payBtn = document.querySelector('#pay_btn');
        if (payBtn) payBtn.disabled = false;
    }
};



     return (
        <div className="row wrapper">
            <div className="col-10 col-lg-5">
                <form onSubmit={submitHandler} className="shadow-lg">
                    <h1 className="mb-4">Card Info</h1>
                    <div className="form-group">
                    <label htmlFor="card_num_field">Card Number</label>
                    <CardNumberElement
                        type="text"
                        id="card_num_field"
                        className="form-control"
                       
                    />
                    </div>
                    
                    <div className="form-group">
                    <label htmlFor="card_exp_field">Card Expiry</label>
                    <CardExpiryElement
                        type="text"
                        id="card_exp_field"
                        className="form-control"
                       
                    />
                    </div>
                    
                    <div className="form-group">
                    <label htmlFor="card_cvc_field">Card CVC</label>
                    <CardCvcElement
                        type="text"
                        id="card_cvc_field"
                        className="form-control"
                        value=""
                    />
                    </div>
        
                
                    <button
                    id="pay_btn"
                    type="submit"
                    className="btn btn-block py-3"
                    >
                    Pay - { ` $${orderInfo && orderInfo.totalPrice}` }
                    </button>
        
                </form>
            </div>
        </div>
    )
}