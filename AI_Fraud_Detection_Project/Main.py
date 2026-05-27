from fraud_detection import load_and_clean_data
from model import train_model

if __name__ == "__main__":
    df, features = load_and_clean_data("data/government_scheme_data.csv")
    model = train_model(features)

    # Predict fraud (-1 = Fraud, 1 = Normal)
    df['Fraud_Prediction'] = model.predict(features)

    print(df)
