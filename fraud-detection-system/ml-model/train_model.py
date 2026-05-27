import numpy as np
from sklearn.ensemble import IsolationForest
from sklearn.datasets import make_classification
import joblib

X, _ = make_classification(n_samples=200, n_features=5, random_state=42)
model = IsolationForest(random_state=42)
model.fit(X)
joblib.dump(model, 'fraud_model.pkl')
print('Model saved to fraud_model.pkl')
