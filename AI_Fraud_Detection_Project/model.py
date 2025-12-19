from sklearn.ensemble import IsolationForest

def train_model(data):
    model = IsolationForest(
        n_estimators=100,
        contamination=0.25,
        random_state=42
    )
    
    model.fit(data)
    return model
