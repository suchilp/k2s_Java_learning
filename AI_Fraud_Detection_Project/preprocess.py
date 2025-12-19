import pandas as pd

def load_and_clean_data(file_path):
    df = pd.read_csv(file_path)
    
    # Selecting numerical columns for AI model
    features = df[['Age', 'Monthly_Income', 'Amount_Received']]
    
    return df, features
