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

@app.post('/evaluate')
def evaluate(features: list[float]):
    if model is None:
        return {'error': 'Model not loaded'}
    prediction = model.predict([features])
    return {'prediction': int(prediction[0])}
