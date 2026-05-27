import pandas as pd
from sklearn.ensemble import IsolationForest

# Load dataset
data = pd.read_csv("data\government_scheme_data.csv")

# Select features
features = data[['Age', 'Monthly_Income', 'Amount_Received']]

# Train model
model = IsolationForest(contamination=0.25, random_state=42)
data['Fraud_Flag'] = model.fit_predict(features)

# Convert prediction
data['Fraud_Flag'] = data['Fraud_Flag'].map({1: 'Genuine', -1: 'Fraud'})

# Display output
print(data)

# Save final output
data.to_csv("fraud_detection_output.csv", index=False)