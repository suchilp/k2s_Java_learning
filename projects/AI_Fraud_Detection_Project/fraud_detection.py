from preprocess import load_and_clean_data
from model import train_model

df, features = load_and_clean_data("data/government_scheme_data.csv")
model = train_model(features)

df['Fraud_Result'] = model.predict(features)
df['Fraud_Result'] = df['Fraud_Result'].map({1:'Genuine Beneficiary', -1:'Potential Fraud'})

print(df)
df.to_csv("fraud_detection_output.csv", index=False)
