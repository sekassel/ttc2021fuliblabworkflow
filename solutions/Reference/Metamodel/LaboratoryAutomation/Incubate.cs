//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated by a tool.
//     Runtime Version:4.0.30319.42000
//
//     Changes to this file may cause incorrect behavior and will be lost if
//     the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

using NMF.Collections.Generic;
using NMF.Collections.ObjectModel;
using NMF.Expressions;
using NMF.Expressions.Linq;
using NMF.Models;
using NMF.Models.Collections;
using NMF.Models.Expressions;
using NMF.Models.Meta;
using NMF.Models.Repository;
using NMF.Serialization;
using NMF.Utilities;
using System;
using System.Collections;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Collections.Specialized;
using System.ComponentModel;
using System.Diagnostics;
using System.Linq;

namespace TTC2021.LabWorkflows.LaboratoryAutomation
{
    
    
    /// <summary>
    /// The default implementation of the Incubate class
    /// </summary>
    [XmlNamespaceAttribute("http://www.transformation-tool-contest.eu/ttc21/laboratoryAutomation")]
    [XmlNamespacePrefixAttribute("lab")]
    [ModelRepresentationClassAttribute("http://www.transformation-tool-contest.eu/ttc21/laboratoryAutomation#//Incubate")]
    public partial class Incubate : ProtocolStep, IIncubate, IModelElement
    {
        
        /// <summary>
        /// The backing field for the Temperature property
        /// </summary>
        [DebuggerBrowsableAttribute(DebuggerBrowsableState.Never)]
        private double _temperature = 29315D;
        
        private static Lazy<ITypedElement> _temperatureAttribute = new Lazy<ITypedElement>(RetrieveTemperatureAttribute);
        
        /// <summary>
        /// The backing field for the Duration property
        /// </summary>
        [DebuggerBrowsableAttribute(DebuggerBrowsableState.Never)]
        private int _duration;
        
        private static Lazy<ITypedElement> _durationAttribute = new Lazy<ITypedElement>(RetrieveDurationAttribute);
        
        private static IClass _classInstance;
        
        /// <summary>
        /// The temperature property
        /// </summary>
        [DefaultValueAttribute(29315D)]
        [DisplayNameAttribute("temperature")]
        [CategoryAttribute("Incubate")]
        [XmlElementNameAttribute("temperature")]
        [XmlAttributeAttribute(true)]
        public double Temperature
        {
            get
            {
                return this._temperature;
            }
            set
            {
                if ((this._temperature != value))
                {
                    double old = this._temperature;
                    ValueChangedEventArgs e = new ValueChangedEventArgs(old, value);
                    this.OnTemperatureChanging(e);
                    this.OnPropertyChanging("Temperature", e, _temperatureAttribute);
                    this._temperature = value;
                    this.OnTemperatureChanged(e);
                    this.OnPropertyChanged("Temperature", e, _temperatureAttribute);
                }
            }
        }
        
        /// <summary>
        /// The duration property
        /// </summary>
        [DisplayNameAttribute("duration")]
        [CategoryAttribute("Incubate")]
        [XmlElementNameAttribute("duration")]
        [XmlAttributeAttribute(true)]
        public int Duration
        {
            get
            {
                return this._duration;
            }
            set
            {
                if ((this._duration != value))
                {
                    int old = this._duration;
                    ValueChangedEventArgs e = new ValueChangedEventArgs(old, value);
                    this.OnDurationChanging(e);
                    this.OnPropertyChanging("Duration", e, _durationAttribute);
                    this._duration = value;
                    this.OnDurationChanged(e);
                    this.OnPropertyChanged("Duration", e, _durationAttribute);
                }
            }
        }
        
        /// <summary>
        /// Gets the Class model for this type
        /// </summary>
        public new static IClass ClassInstance
        {
            get
            {
                if ((_classInstance == null))
                {
                    _classInstance = ((IClass)(MetaRepository.Instance.Resolve("http://www.transformation-tool-contest.eu/ttc21/laboratoryAutomation#//Incubate")));
                }
                return _classInstance;
            }
        }
        
        /// <summary>
        /// Gets fired before the Temperature property changes its value
        /// </summary>
        public event System.EventHandler<ValueChangedEventArgs> TemperatureChanging;
        
        /// <summary>
        /// Gets fired when the Temperature property changed its value
        /// </summary>
        public event System.EventHandler<ValueChangedEventArgs> TemperatureChanged;
        
        /// <summary>
        /// Gets fired before the Duration property changes its value
        /// </summary>
        public event System.EventHandler<ValueChangedEventArgs> DurationChanging;
        
        /// <summary>
        /// Gets fired when the Duration property changed its value
        /// </summary>
        public event System.EventHandler<ValueChangedEventArgs> DurationChanged;
        
        private static ITypedElement RetrieveTemperatureAttribute()
        {
            return ((ITypedElement)(((ModelElement)(TTC2021.LabWorkflows.LaboratoryAutomation.Incubate.ClassInstance)).Resolve("temperature")));
        }
        
        /// <summary>
        /// Raises the TemperatureChanging event
        /// </summary>
        /// <param name="eventArgs">The event data</param>
        protected virtual void OnTemperatureChanging(ValueChangedEventArgs eventArgs)
        {
            System.EventHandler<ValueChangedEventArgs> handler = this.TemperatureChanging;
            if ((handler != null))
            {
                handler.Invoke(this, eventArgs);
            }
        }
        
        /// <summary>
        /// Raises the TemperatureChanged event
        /// </summary>
        /// <param name="eventArgs">The event data</param>
        protected virtual void OnTemperatureChanged(ValueChangedEventArgs eventArgs)
        {
            System.EventHandler<ValueChangedEventArgs> handler = this.TemperatureChanged;
            if ((handler != null))
            {
                handler.Invoke(this, eventArgs);
            }
        }
        
        private static ITypedElement RetrieveDurationAttribute()
        {
            return ((ITypedElement)(((ModelElement)(TTC2021.LabWorkflows.LaboratoryAutomation.Incubate.ClassInstance)).Resolve("duration")));
        }
        
        /// <summary>
        /// Raises the DurationChanging event
        /// </summary>
        /// <param name="eventArgs">The event data</param>
        protected virtual void OnDurationChanging(ValueChangedEventArgs eventArgs)
        {
            System.EventHandler<ValueChangedEventArgs> handler = this.DurationChanging;
            if ((handler != null))
            {
                handler.Invoke(this, eventArgs);
            }
        }
        
        /// <summary>
        /// Raises the DurationChanged event
        /// </summary>
        /// <param name="eventArgs">The event data</param>
        protected virtual void OnDurationChanged(ValueChangedEventArgs eventArgs)
        {
            System.EventHandler<ValueChangedEventArgs> handler = this.DurationChanged;
            if ((handler != null))
            {
                handler.Invoke(this, eventArgs);
            }
        }
        
        /// <summary>
        /// Resolves the given attribute name
        /// </summary>
        /// <returns>The attribute value or null if it could not be found</returns>
        /// <param name="attribute">The requested attribute name</param>
        /// <param name="index">The index of this attribute</param>
        protected override object GetAttributeValue(string attribute, int index)
        {
            if ((attribute == "TEMPERATURE"))
            {
                return this.Temperature;
            }
            if ((attribute == "DURATION"))
            {
                return this.Duration;
            }
            return base.GetAttributeValue(attribute, index);
        }
        
        /// <summary>
        /// Sets a value to the given feature
        /// </summary>
        /// <param name="feature">The requested feature</param>
        /// <param name="value">The value that should be set to that feature</param>
        protected override void SetFeature(string feature, object value)
        {
            if ((feature == "TEMPERATURE"))
            {
                this.Temperature = ((double)(value));
                return;
            }
            if ((feature == "DURATION"))
            {
                this.Duration = ((int)(value));
                return;
            }
            base.SetFeature(feature, value);
        }
        
        /// <summary>
        /// Gets the property expression for the given attribute
        /// </summary>
        /// <returns>An incremental property expression</returns>
        /// <param name="attribute">The requested attribute in upper case</param>
        protected override NMF.Expressions.INotifyExpression<object> GetExpressionForAttribute(string attribute)
        {
            if ((attribute == "TEMPERATURE"))
            {
                return Observable.Box(new TemperatureProxy(this));
            }
            if ((attribute == "DURATION"))
            {
                return Observable.Box(new DurationProxy(this));
            }
            return base.GetExpressionForAttribute(attribute);
        }
        
        /// <summary>
        /// Gets the Class for this model element
        /// </summary>
        public override IClass GetClass()
        {
            if ((_classInstance == null))
            {
                _classInstance = ((IClass)(MetaRepository.Instance.Resolve("http://www.transformation-tool-contest.eu/ttc21/laboratoryAutomation#//Incubate")));
            }
            return _classInstance;
        }
        
        /// <summary>
        /// Represents a proxy to represent an incremental access to the temperature property
        /// </summary>
        private sealed class TemperatureProxy : ModelPropertyChange<IIncubate, double>
        {
            
            /// <summary>
            /// Creates a new observable property access proxy
            /// </summary>
            /// <param name="modelElement">The model instance element for which to create the property access proxy</param>
            public TemperatureProxy(IIncubate modelElement) : 
                    base(modelElement, "temperature")
            {
            }
            
            /// <summary>
            /// Gets or sets the value of this expression
            /// </summary>
            public override double Value
            {
                get
                {
                    return this.ModelElement.Temperature;
                }
                set
                {
                    this.ModelElement.Temperature = value;
                }
            }
        }
        
        /// <summary>
        /// Represents a proxy to represent an incremental access to the duration property
        /// </summary>
        private sealed class DurationProxy : ModelPropertyChange<IIncubate, int>
        {
            
            /// <summary>
            /// Creates a new observable property access proxy
            /// </summary>
            /// <param name="modelElement">The model instance element for which to create the property access proxy</param>
            public DurationProxy(IIncubate modelElement) : 
                    base(modelElement, "duration")
            {
            }
            
            /// <summary>
            /// Gets or sets the value of this expression
            /// </summary>
            public override int Value
            {
                get
                {
                    return this.ModelElement.Duration;
                }
                set
                {
                    this.ModelElement.Duration = value;
                }
            }
        }
    }
}

