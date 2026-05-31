from fastapi import FastAPI
import joblib
import numpy as np

app = FastAPI()

try:
    model = joblib.load('fraud_model.pkl')
except Exception:
    model = None

@app.get('/')
def root():
    return {'message': 'ML model service is running'}

@app.post('/predict')
def predict(amount: float):
    if model is None:
        return {'error': 'Model not loaded'}
    prediction = model.predict([[amount]])
    return {'prediction': int(prediction[0])}
